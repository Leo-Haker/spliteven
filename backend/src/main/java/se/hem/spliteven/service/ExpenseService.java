package se.hem.spliteven.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import se.hem.spliteven.mapper.DtoMapper;
import se.hem.spliteven.model.Account;
import se.hem.spliteven.model.Expense;
import se.hem.spliteven.model.Person;
import se.hem.spliteven.repository.AccountRepository;
import se.hem.spliteven.repository.ExpenseRepository;
import se.hem.spliteven.repository.PersonRepository;

@Service
public class ExpenseService extends AbstractService {

    public ExpenseService(
            ExpenseRepository expenseRepository,
            AccountRepository accountRepository,
            PersonRepository personRepository) {
        super(expenseRepository, accountRepository, personRepository);
    }

    public Expense create(
            Long accountId,
            Long personId,
            boolean income,
            String description,
            BigDecimal amount,
            LocalDate date) {
        Account account = findAccountById(accountId);
        Person person = findPersonById(personId);
        Expense expense = new Expense(account, person, income, description, amount, date);
        return expenseRepository.save(expense);
    }

    public List<Expense> getByAccount(Long accountId) {
        return expenseRepository.findByAccountId(accountId);
    }
}
