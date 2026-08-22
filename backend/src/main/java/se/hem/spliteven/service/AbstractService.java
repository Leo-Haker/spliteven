package se.hem.spliteven.service;

import org.springframework.stereotype.Service;

import se.hem.spliteven.model.Account;
import se.hem.spliteven.model.MembershipRequest;
import se.hem.spliteven.model.Person;
import se.hem.spliteven.repository.AccountRepository;
import se.hem.spliteven.repository.ExpenseRepository;
import se.hem.spliteven.repository.PersonRepository;

@Service
public abstract class AbstractService {

    protected ExpenseRepository expenseRepository;
    protected AccountRepository accountRepository;
    protected PersonRepository personRepository;
    protected String accountNotFound = "Kunde inte hitta konto";
    protected String personNotFound = "Kunde inte hitta person";
    protected String expenseNotFound = "Kunde inte hitta utgift";

    protected AbstractService(
            ExpenseRepository expenseRepository,
            AccountRepository accountRepository,
            PersonRepository personRepository) {
        this.expenseRepository = expenseRepository;
        this.accountRepository = accountRepository;
        this.personRepository = personRepository;
    }

    protected Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException(accountNotFound));
    }

    protected Person findPersonById(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException(personNotFound));
    }

    protected Person findPersonByEmail(String email) {
        return personRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Ingen användare med den mailadressen"));
    }

  

}
