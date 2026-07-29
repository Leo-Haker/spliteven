package se.hem.spliteven.controller;

import se.hem.spliteven.dto.AccountBalanceDto;
import se.hem.spliteven.dto.AccountDto;
import se.hem.spliteven.dto.PersonDto;
import se.hem.spliteven.model.Account;
import se.hem.spliteven.model.Expense;
import se.hem.spliteven.model.Person;
import se.hem.spliteven.repository.PersonRepository;
import se.hem.spliteven.repository.AccountRepository;
import se.hem.spliteven.repository.ExpenseRepository;
import se.hem.spliteven.service.BalanceCalculator;

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

    public PersonController(PersonRepository personRepository,
            ExpenseRepository expenseRepository, AccountRepository accountRepository) {
        this.personRepository = personRepository;
        this.expenseRepository = expenseRepository;
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public PersonDto create(@Valid @RequestBody Person person) {
        Person saved = personRepository.save(person);
        return toDto(saved);
    }

    @GetMapping
    public List<PersonDto> getAll() {
        return personRepository.findAll().stream().map(this::toDto).toList();
    }

    private PersonDto toDto(Person p) {
        return new PersonDto(p.getId(), p.getName(), p.getEmail());
    }

    @GetMapping("/{personId}/accounts")
    public List<AccountDto> getAccountsForPerson(@PathVariable Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person hittades inte"));

        return person.getAccounts().stream().map(this::accountToDto).toList();
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
