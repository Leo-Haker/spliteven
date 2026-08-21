package se.hem.spliteven.controller;

import se.hem.spliteven.domain.BalanceCalculator;
import se.hem.spliteven.dto.AccountBalanceDto;
import se.hem.spliteven.dto.AccountDto;
import se.hem.spliteven.dto.CreatePersonRequest;
import se.hem.spliteven.dto.PersonDto;
import se.hem.spliteven.dto.RenameUserRequest;
import se.hem.spliteven.model.Account;
import se.hem.spliteven.model.Expense;
import se.hem.spliteven.model.Person;
import se.hem.spliteven.repository.PersonRepository;
import se.hem.spliteven.service.PersonService;
import se.hem.spliteven.repository.AccountRepository;
import se.hem.spliteven.repository.ExpenseRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonRepository personRepository;
    private final ExpenseRepository expenseRepository;
    private final AccountRepository accountRepository;
    private final PersonService personService;

    public PersonController(PersonRepository personRepository, PersonService personService,
            ExpenseRepository expenseRepository, AccountRepository accountRepository) {
        this.personRepository = personRepository;
        this.expenseRepository = expenseRepository;
        this.accountRepository = accountRepository;
        this.personService = personService;
    }

    @PostMapping
    public PersonDto create(@Valid @RequestBody CreatePersonRequest request) {

        Person saved = personService.createPerson(request);

        return toDto(saved);
    }

    @GetMapping
    public List<PersonDto> getAll() {
        return personRepository.findAll().stream().map(this::toDto).toList();
    }

    private PersonDto toDto(Person p) {
        return new PersonDto(p.getId(), p.getName(), p.getEmail());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> rename(@PathVariable Long id, @RequestBody RenameUserRequest request) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Användare hittades inte"));
        person.setName(request.name());
        Person saved = personRepository.save(person);
        return ResponseEntity.ok(toDto(saved));
    }

    @GetMapping("/by-email")
    public ResponseEntity<PersonDto> getByEmail(@RequestParam String email) {
        return personRepository.findByEmail(email)
                .map(p -> ResponseEntity.ok(toDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{personId}/accounts")
    public List<AccountDto> getAccountsForPerson(@PathVariable Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person hittades inte"));

        return person.getAccounts().stream().map(this::accountToDto).toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Person hittades inte"));

        checkNotNegativeBalanceOnAccounts(person);
        removePersonFromTheirAccounts(person);

        personRepository.delete(person);
        return ResponseEntity.noContent().build();

    }

    private void checkNotNegativeBalanceOnAccounts(Person person) {
        for (Account account : person.getAccounts()) {
            List<Expense> expenses = expenseRepository.findByAccountId(account.getId());
            BalanceCalculator calculator = new BalanceCalculator(account);
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

    @GetMapping("/{personId}/balances")
    public List<AccountBalanceDto> getBalancesForPerson(
            @PathVariable Long personId,
            @RequestParam String from, // format: "2026-07"
            @RequestParam String to) {

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person hittades inte"));

        YearMonth fromMonth = YearMonth.parse(from);
        YearMonth toMonth = YearMonth.parse(to);

        return person.getAccounts().stream().map(account -> {
            List<Expense> expenses = expenseRepository.findByAccountId(account.getId());

            BalanceCalculator calculator = new BalanceCalculator(account);
            BigDecimal balance = calculator.calculateBalanceForPerson(person, expenses, fromMonth, toMonth);
            return new AccountBalanceDto(account.getId(), account.getName(), balance);
        }).toList();
    }

    private AccountDto accountToDto(Account a) {
        List<PersonDto> members = a.getPersons().stream()
                .map(p -> new PersonDto(p.getId(), p.getName(), p.getEmail()))
                .toList();
        return new AccountDto(a.getId(), a.getName(), members);
    }

}
