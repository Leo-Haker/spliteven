package se.hem.spliteven.controller;

import se.hem.spliteven.dto.AccountDto;
import se.hem.spliteven.dto.PersonDto;
import se.hem.spliteven.dto.RenameAccountRequest;
import se.hem.spliteven.model.Account;
import se.hem.spliteven.model.Person;
import se.hem.spliteven.repository.AccountRepository;
import se.hem.spliteven.repository.PersonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountRepository accountRepository;
    private final PersonRepository personRepository;

    public AccountController(AccountRepository accountRepository, PersonRepository personRepository) {
        this.accountRepository = accountRepository;
        this.personRepository = personRepository;
    }

    public record CreateAccountRequest(String name, Long personId) {
    }

    @PostMapping
    public ResponseEntity<AccountDto> create(@RequestBody CreateAccountRequest request) {
        Person creator = personRepository.findById(request.personId())
                .orElseThrow(() -> new IllegalArgumentException("Person hittades inte"));

        Account account = new Account(request.name());
        account.addPerson(creator);

        Account saved = accountRepository.save(account);
        return ResponseEntity.ok(toDto(saved));
    }

    @PostMapping("/{accountId}/members/{personId}")
    public ResponseEntity<AccountDto> addMember(@PathVariable Long accountId, @PathVariable Long personId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Konto hittades inte"));
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person hittades inte"));

        account.addPerson(person);
        Account saved = accountRepository.save(account);
        return ResponseEntity.ok(toDto(saved));
    }

    @GetMapping
    public List<AccountDto> getAll() {
        return accountRepository.findAll().stream().map(this::toDto).toList();
    }

    private AccountDto toDto(Account a) {
        List<PersonDto> members = a.getPersons().stream()
                .map(p -> new PersonDto(p.getId(), p.getName(), p.getEmail()))
                .toList();
        return new AccountDto(a.getId(), a.getName(), members);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> rename(@PathVariable Long id, @RequestBody RenameAccountRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Konto hittades inte"));
        account.setName(request.name());
        Account saved = accountRepository.save(account);
        return ResponseEntity.ok(toDto(saved));
    }

    @DeleteMapping("/{accountId}/members/{personId}")
    public ResponseEntity<AccountDto> removeMember(@PathVariable Long accountId, @PathVariable Long personId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Konto hittades inte"));
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("Person hittades inte"));

        account.removePerson(person);
        Account saved = accountRepository.save(account);
        return ResponseEntity.ok(toDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getOne(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(a -> ResponseEntity.ok(toDto(a)))
                .orElse(ResponseEntity.notFound().build());
    }
}