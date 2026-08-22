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

import org.springframework.stereotype.Service;

@Service
public class AccountService extends AbstractService {

    public AccountService(
            ExpenseRepository expenseRepository,
            AccountRepository accountRepository,
            PersonRepository personRepository) {
        super(expenseRepository, accountRepository, personRepository);
    }

    public Account create(String name, Long createrId) {
        Person creater = findPersonById(createrId);
        Account account = new Account(name);
        account.addPerson(creater);
        return accountRepository.save(account);
    }

    public Account addMember(Long accountId, Long personId) {
        Account account = findAccountById(accountId);
        Person person = findPersonById(personId);
        account.addPerson(person);
        return accountRepository.save(account);

    }

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account rename(Long id, String name) {
        Account account = findAccountById(id);
        account.setName(name);
        return accountRepository.save(account);
    }

    public Account removeMember(Long accountId, Long personId) {
        Account account = findAccountById(accountId);
        Person person = findPersonById(personId);
        account.removePerson(person);
        return accountRepository.save(account);
    }

    public Optional<Account> getOne(Long id) {
        return accountRepository.findById(id);
    }

}
