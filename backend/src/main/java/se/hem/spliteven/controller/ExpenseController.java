package se.hem.spliteven.controller;

import se.hem.spliteven.dto.CreateExpenseRequest;
import se.hem.spliteven.dto.ExpenseDto;
import se.hem.spliteven.model.Account;
import se.hem.spliteven.model.Expense;
import se.hem.spliteven.model.Person;
import se.hem.spliteven.repository.AccountRepository;
import se.hem.spliteven.repository.ExpenseRepository;
import se.hem.spliteven.repository.PersonRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final AccountRepository accountRepository;
    private final PersonRepository personRepository;

    public ExpenseController(ExpenseRepository expenseRepository,
            AccountRepository accountRepository,
            PersonRepository personRepository) {
        this.expenseRepository = expenseRepository;
        this.accountRepository = accountRepository;
        this.personRepository = personRepository;
    }

    @PostMapping
    public ExpenseDto create(@RequestBody CreateExpenseRequest request) {
        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new IllegalArgumentException("Konto hittades inte"));
        Person paidBy = personRepository.findById(request.paidById())
                .orElseThrow(() -> new IllegalArgumentException("Person hittades inte"));

        Expense expense = new Expense(account, paidBy, request.income(),
                request.description(), request.amount(), request.date());

        Expense saved = expenseRepository.save(expense);
        return toDto(saved);
    }

    @GetMapping("/account/{accountId}")
    public List<ExpenseDto> getByAccount(@PathVariable Long accountId) {
        return expenseRepository.findByAccountId(accountId).stream().map(this::toDto).toList();
    }

    private ExpenseDto toDto(Expense e) {
        return new ExpenseDto(e.getId(), e.getDescription(), e.getAmount(),
                e.isIncome(), e.getPaidBy().getName(), e.getDate());
    }
}