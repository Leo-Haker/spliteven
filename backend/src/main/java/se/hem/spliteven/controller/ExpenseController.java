package se.hem.spliteven.controller;

import se.hem.spliteven.dto.CreateExpenseRequest;
import se.hem.spliteven.dto.ExpenseDto;
import se.hem.spliteven.mapper.DtoMapper;
import se.hem.spliteven.model.Account;
import se.hem.spliteven.model.Expense;
import se.hem.spliteven.model.Person;
import se.hem.spliteven.repository.AccountRepository;
import se.hem.spliteven.repository.ExpenseRepository;
import se.hem.spliteven.repository.PersonRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final AccountRepository accountRepository;
    private final PersonRepository personRepository;
    private final DtoMapper dtoMapper;

    public ExpenseController(ExpenseRepository expenseRepository,
            AccountRepository accountRepository,
            PersonRepository personRepository,
            DtoMapper dtoMapper) {
        this.expenseRepository = expenseRepository;
        this.accountRepository = accountRepository;
        this.personRepository = personRepository;
        this.dtoMapper = dtoMapper;
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
        return dtoMapper.toDto(saved);
    }

    @GetMapping("/account/{accountId}")
    public List<ExpenseDto> getByAccount(@PathVariable Long accountId) {
        return expenseRepository.findByAccountId(accountId).stream().map(account -> dtoMapper.toDto(account)).toList();
    }
}