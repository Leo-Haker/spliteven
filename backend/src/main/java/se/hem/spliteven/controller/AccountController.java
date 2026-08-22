package se.hem.spliteven.controller;

import se.hem.spliteven.dto.AccountBalanceDto;
import se.hem.spliteven.dto.AccountDto;
import se.hem.spliteven.dto.ExpenseDto;
import se.hem.spliteven.dto.PersonDto;
import se.hem.spliteven.dto.RenameAccountRequest;
import se.hem.spliteven.model.Account;
import se.hem.spliteven.dto.CreateAccountRequest;
import se.hem.spliteven.service.AccountService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDto> create(@RequestBody CreateAccountRequest request) {
        Account account = accountService.create(request.name(), request.personId());
        return ResponseEntity.ok(toDto(account));
    }

    @PostMapping("/{accountId}/members/{personId}")
    public ResponseEntity<AccountDto> addMember(@PathVariable Long accountId, @PathVariable Long personId) {
        Account account = accountService.addMember(accountId, personId);
        return ResponseEntity.ok(toDto(account));
    }

    @GetMapping
    public List<AccountDto> getAll() {
        return accountService.getAll().stream().map(this::toDto).toList();
    }

    private AccountDto toDto(Account a) {
        List<PersonDto> members = a.getPersons().stream()
                .map(p -> new PersonDto(p.getId(), p.getName(), p.getEmail()))
                .toList();
        return new AccountDto(a.getId(), a.getName(), members);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> rename(@PathVariable Long id, @RequestBody RenameAccountRequest request) {
        Account account = accountService.rename(id, request.name());
        return ResponseEntity.ok(toDto(account));
    }

    @DeleteMapping("/{accountId}/members/{personId}")
    public ResponseEntity<AccountDto> removeMember(@PathVariable Long accountId, @PathVariable Long personId) {
        Account account = accountService.removeMember(accountId, personId);
        return ResponseEntity.ok(toDto(account));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getOne(@PathVariable Long id) {
        return accountService.getOne(id)
                .map(a -> ResponseEntity.ok(toDto(a)))
                .orElse(ResponseEntity.notFound().build());
    }

}