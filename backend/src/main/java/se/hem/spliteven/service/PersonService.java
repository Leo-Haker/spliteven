package se.hem.spliteven.service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import se.hem.spliteven.domain.BalanceCalculator;
import se.hem.spliteven.dto.AccountBalanceDto;
import se.hem.spliteven.dto.AccountDto;
import se.hem.spliteven.dto.CreatePersonRequest;
import se.hem.spliteven.dto.PersonDto;
import se.hem.spliteven.model.Account;
import se.hem.spliteven.model.Expense;
import se.hem.spliteven.model.Person;
import se.hem.spliteven.repository.AccountRepository;
import se.hem.spliteven.repository.ExpenseRepository;
import se.hem.spliteven.repository.PersonRepository;

@Service
public class PersonService extends AbstractService {

    private final PasswordEncoder passwordEncoder;
    private final String errorLogin = "Fel e-post eller lösenord";

    public PersonService(
            PersonRepository personRepository,
            ExpenseRepository expenseRepository,
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder) {
        super(expenseRepository, accountRepository, personRepository);
        this.passwordEncoder = passwordEncoder;
    }

    public Person createPerson(CreatePersonRequest request) {
        String passwordHash = passwordEncoder.encode(request.password());

        Person person = new Person(request.name(), request.email(), passwordHash);

        return personRepository.save(person);
    }

    public Person login(String email, String password) throws IllegalArgumentException {
        Person person = findPersonByEmail(email);

        if (!passwordEncoder.matches(password, person.getPasswordHash())) {
            throw new IllegalArgumentException(errorLogin);
        }

        return person;
    }

    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public Person getPerson(Long id) {
        return findPersonById(id);
    }

    public Optional<Person> findByEmail(String email) {
        return findByEmail(email);
    }

    public Person rename(Long id, String name) {
        Person person = getPerson(id);
        person.setName(name);
        return personRepository.save(person);
    }

    public List<Account> getAccountsForPerson(Long personId) {
        return findPersonById(personId).getAccounts();
    }

    public void deletePerson(Long id) {
        Person person = findPersonById(id);

        checkNotNegativeBalanceOnAccounts(person);
        removePersonFromTheirAccounts(person);

        personRepository.delete(person);
    }

    private void checkNotNegativeBalanceOnAccounts(Person person) {
        for (Account account : person.getAccounts()) {
            List<Expense> expenses = expenseRepository.findByAccountId(account.getId());
            BalanceCalculator calculator = new BalanceCalculator(account);
            // A member can only be removed after settling debts from the complete expense
            // history.
            YearMonth earliestDate = expenses.stream()
                    .map(e -> YearMonth.from(e.getDate()))
                    .min(YearMonth::compareTo)
                    .orElse(YearMonth.now());
            YearMonth latestDate = YearMonth.now();

            BigDecimal balance = calculator.calculateBalanceForPerson(person, expenses, earliestDate, latestDate);

            if (balance.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalStateException(
                        "Kan inte ta bort användaren - skulder finns kvar i kontot \"" + account.getName() + "\"");
            }
        }
    }

    private void removePersonFromTheirAccounts(Person person) {
        for (Account account : List.copyOf(person.getAccounts())) {
            account.removePerson(person);
        }
    }

    // Date format: "2026-07"
    public Map<Account, BigDecimal> getBalancesForPerson(Long personId, YearMonth from, YearMonth to) {

        Person person = findPersonById(personId);

        Map<Account, BigDecimal> balances = new HashMap<>();

        for (Account account : person.getAccounts()) {
            List<Expense> expenses = expenseRepository.findByAccountId(account.getId());

            BalanceCalculator calculator = new BalanceCalculator(account);
            BigDecimal balance = calculator.calculateBalanceForPerson(person, expenses, from, to);

            balances.put(account, balance);
        }

        return balances;
    }

}
