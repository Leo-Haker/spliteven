package se.hem.spliteven.mapper;

import se.hem.spliteven.dto.AccountBalanceDto;
import se.hem.spliteven.dto.AccountDto;
import se.hem.spliteven.dto.ExpenseDto;
import se.hem.spliteven.dto.PersonDto;
import se.hem.spliteven.model.Account;
import se.hem.spliteven.model.Expense;
import se.hem.spliteven.model.Person;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public PersonDto toDto(Person p) {
        return new PersonDto(p.getId(), p.getName(), p.getEmail());
    }

    public AccountDto toDto(Account account) {
        return new AccountDto(
                account.getId(),
                account.getName(),
                account.getPersons().stream().map(this::toDto).toList());
    }

    public ExpenseDto toDto(Expense expense) {
        return new ExpenseDto(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.isIncome(),
                expense.getPaidBy().getId(),
                expense.getPaidBy().getName(),
                expense.getDate());
    }

    public AccountBalanceDto toDto(Account account, BigDecimal balance) {
        return new AccountBalanceDto(account.getId(), account.getName(), balance);
    }

    public List<AccountBalanceDto> toListOfDtos(Map<Account, BigDecimal> balances) {
        return balances.entrySet()
                .stream()
                .map(entry -> toDto(entry.getKey(), entry.getValue()))
                .toList();
    }

}
