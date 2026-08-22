package se.hem.spliteven.service;

import se.hem.spliteven.domain.BalanceCalculator;
import se.hem.spliteven.model.Account;
import se.hem.spliteven.model.Expense;
import se.hem.spliteven.model.Person;
import se.hem.spliteven.repository.AccountRepository;
import se.hem.spliteven.repository.ExpenseRepository;
import se.hem.spliteven.repository.PersonRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public class AccountService {

    private final ExpenseRepository expenseRepository;
    private final AccountRepository accountRepository;
    private final PersonRepository personRepository;
    private String accountNotFound = "Kunde inte hitta konto";
    private String personNotFound = "Kunde inte hitta person";

    public AccountService(
            ExpenseRepository expenseRepository,
            AccountRepository accountRepository,
            PersonRepository personRepository) {
        this.expenseRepository = expenseRepository;
        this.accountRepository = accountRepository;
        this.personRepository = personRepository;
    }

    public Account create(String name, Long createrId) {
        Person creater = personRepository.findById(createrId)
                .orElseThrow(() -> new IllegalArgumentException(personNotFound));
        Account account = new Account(name);
        account.addPerson(creater);
        return accountRepository.save(account);
    }

    public Account addMember(Long accountId, Long personId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException(accountNotFound));
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException(personNotFound));
        account.addPerson(person);
        return accountRepository.save(account);

    }

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account rename(Long id, String name) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(accountNotFound));
        account.setName(name);
        return accountRepository.save(account);
    }

    public Account removeMember(Long accountId, Long personId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException(accountNotFound));
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException(personNotFound));
        account.removePerson(person);
        return accountRepository.save(account);
    }

    public Optional<Account> getOne(Long id) {
        return accountRepository.findById(id);
    }

}
