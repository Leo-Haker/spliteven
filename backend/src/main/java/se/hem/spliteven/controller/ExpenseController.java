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
import se.hem.spliteven.service.ExpenseService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final DtoMapper dtoMapper;

    public ExpenseController(
            ExpenseService expenseService,
            DtoMapper dtoMapper) {
        this.expenseService = expenseService;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping
    public ExpenseDto create(@RequestBody CreateExpenseRequest request) {
        Expense expense = expenseService.create(
                request.accountId(),
                request.paidById(),
                request.income(),
                request.description(),
                request.amount(),
                request.date());

        return dtoMapper.toDto(expense);
    }

    @GetMapping("/account/{accountId}")
    public List<ExpenseDto> getByAccount(@PathVariable Long accountId) {
        return expenseService.getByAccount(accountId)
                .stream()
                .map(dtoMapper::toDto)
                .toList();
    }
}