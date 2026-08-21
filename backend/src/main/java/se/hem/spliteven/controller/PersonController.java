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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public PersonDto create(@Valid @RequestBody CreatePersonRequest request) {

        Person saved = personService.createPerson(request);

        return toDto(saved);
    }

    @GetMapping
    public List<PersonDto> getAll() {
        return personService.getAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> rename(@PathVariable Long id, @RequestBody RenameUserRequest request) {
        Person saved = personService.rename(id, request.name());
        return ResponseEntity.ok(toDto(saved));
    }

    @GetMapping("/by-email")
    public ResponseEntity<PersonDto> getByEmail(@RequestParam String email) {
        return personService.findByEmail(email)
                .map(p -> ResponseEntity.ok(toDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{personId}/accounts")
    public List<AccountDto> getAccountsForPerson(@PathVariable Long personId) {
        return personService.getAccountsForPerson(personId)
                .stream()
                .map(this::accountToDto)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{personId}/balances")
    public List<AccountBalanceDto> getBalancesForPerson(
            @PathVariable Long personId,
            @RequestParam String from, // format: "2026-07"
            @RequestParam String to) {

        Map<Account, BigDecimal> balances = personService.getBalancesForPerson(personId, YearMonth.parse(from),
                YearMonth.parse(to));

        return toListOfAccountBalanceDtos(balances);
    }

    private List<AccountBalanceDto> toListOfAccountBalanceDtos(Map<Account, BigDecimal> balances) {
        return balances.entrySet()
                .stream()
                .map((entry) -> {
                    Account account = entry.getKey();
                    BigDecimal balance = entry.getValue();

                    return new AccountBalanceDto(account.getId(), account.getName(), balance);
                })
                .toList();
    }

    private AccountDto accountToDto(Account account) {
        List<PersonDto> persons = account.getPersons().stream().map(this::toDto).toList();
        return new AccountDto(account.getId(), account.getName(), persons);
    }

    private PersonDto toDto(Person p) {
        return new PersonDto(p.getId(), p.getName(), p.getEmail());
    }

}
