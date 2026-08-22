package se.hem.spliteven.controller;

import se.hem.spliteven.dto.AccountBalanceDto;
import se.hem.spliteven.dto.AccountDto;
import se.hem.spliteven.dto.ExpenseDto;
import se.hem.spliteven.dto.PersonDto;
import se.hem.spliteven.dto.RenameAccountRequest;
import se.hem.spliteven.mapper.DtoMapper;
import se.hem.spliteven.model.Account;
import se.hem.spliteven.dto.CreateAccountRequest;
import se.hem.spliteven.service.AccountService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final DtoMapper dtoMapper;

    public AccountController(AccountService accountService, DtoMapper dtoMapper) {
        this.accountService = accountService;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping
    public ResponseEntity<AccountDto> create(@RequestBody CreateAccountRequest request) {
        Account account = accountService.create(request.name(), request.personId());
        return ResponseEntity.ok(dtoMapper.toDto(account));
    }

    @PostMapping("/{accountId}/members/{personId}")
    public ResponseEntity<AccountDto> addMember(@PathVariable Long accountId, @PathVariable Long personId) {
        Account account = accountService.addMember(accountId, personId);
        return ResponseEntity.ok(dtoMapper.toDto(account));
    }

    @GetMapping
    public List<AccountDto> getAll() {
        return accountService
                .getAll()
                .stream()
                .map(dtoMapper::toDto)
                .toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> rename(@PathVariable Long id, @RequestBody RenameAccountRequest request) {
        Account account = accountService.rename(id, request.name());
        return ResponseEntity.ok(dtoMapper.toDto(account));
    }

    @DeleteMapping("/{accountId}/members/{personId}")
    public ResponseEntity<AccountDto> removeMember(@PathVariable Long accountId, @PathVariable Long personId) {
        Account account = accountService.removeMember(accountId, personId);
        return ResponseEntity.ok(dtoMapper.toDto(account));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getOne(@PathVariable Long id) {
        return accountService.getOne(id)
                .map(a -> ResponseEntity.ok(dtoMapper.toDto(a)))
                .orElse(ResponseEntity.notFound().build());
    }

}