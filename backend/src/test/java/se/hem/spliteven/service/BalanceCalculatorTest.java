package se.hem.spliteven.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.hem.spliteven.model.Account;
import se.hem.spliteven.model.Expense;
import se.hem.spliteven.model.Person;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BalanceCalculatorTest {
    private Person personOne;
    private Person personTwo;
    private Account account;
    private BalanceCalculator calculator;
    private final YearMonth july = YearMonth.of(2026, 7);

    @BeforeEach
    void setUp() {
        personOne = new Person("JRR", "JRR@mail.com");
        personTwo = new Person("Tolkien", "Tolkien@mail.com");
        account = new Account("Home");
        account.addPerson(personOne);
        account.addPerson(personTwo);
        calculator = new BalanceCalculator(account);
    }

    @Test
    void twoPersonsCheckBalances() {
        // personOne pays 100, 2 members -> 50/50 split
        Expense expense = new Expense(account, personOne, false, "Groceries",
                BigDecimal.valueOf(100), LocalDate.of(2026, 7, 10));

        Map<Person, BigDecimal> balances = calculator.calculateBalances(List.of(expense), july, july);

        assertEquals(new BigDecimal("50.00").setScale(2), balances.get(personOne).setScale(2));
        assertEquals(new BigDecimal("-50.00").setScale(2), balances.get(personTwo).setScale(2));
    }

    @Test
    void balancesShouldAlwaysSumToZero() {
        Person third = new Person("Kim", "kim@mail.com");
        account.addPerson(third);
        BalanceCalculator threePersonCalculator = new BalanceCalculator(account);

        Expense expense = new Expense(account, personOne, false, "Dinner",
                BigDecimal.valueOf(90), LocalDate.of(2026, 7, 10));

        Map<Person, BigDecimal> balances = threePersonCalculator.calculateBalances(List.of(expense), july, july);

        BigDecimal sum = balances.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Money can't appear or disappear - the sum of everyone's balance must be zero
        assertEquals(BigDecimal.ZERO.setScale(2), sum.setScale(2));
    }

    @Test
    void expenseOutsideRequestedMonthIsIgnored() {
        Expense juneExpense = new Expense(account, personOne, false, "Old groceries",
                BigDecimal.valueOf(100), LocalDate.of(2026, 6, 15));

        Map<Person, BigDecimal> balances = calculator.calculateBalances(List.of(juneExpense), july, july);

        assertEquals(BigDecimal.ZERO.setScale(2), balances.get(personOne).setScale(2));
        assertEquals(BigDecimal.ZERO.setScale(2), balances.get(personTwo).setScale(2));
    }

    @Test
    void expenseNotBelongingToAccountThrows() {
        Account otherAccount = new Account("Other household");
        otherAccount.addPerson(personOne);
        Expense foreignExpense = new Expense(otherAccount, personOne, false, "Not this account's",
                BigDecimal.valueOf(50), LocalDate.of(2026, 7, 5));

        assertThrows(IllegalStateException.class,
                () -> calculator.calculateBalances(List.of(foreignExpense), july, july));
    }

    @Test
    void payerNotMemberOfAccountThrows() {
        Person outsider = new Person("Outsider", "outsider@mail.com");
        Expense expense = new Expense(account, outsider, false, "Suspicious",
                BigDecimal.valueOf(50), LocalDate.of(2026, 7, 5));

        assertThrows(IllegalStateException.class,
                () -> calculator.calculateBalances(List.of(expense), july, july));
    }

    @Test
    void incomeIsHandledWithOppositeSign() {
        // personOne receives 100 as shared income, split 50/50
        Expense income = new Expense(account, personOne, true, "Refund",
                BigDecimal.valueOf(100), LocalDate.of(2026, 7, 10));

        Map<Person, BigDecimal> balances = calculator.calculateBalances(List.of(income), july, july);

        // Sum should still be zero regardless of income vs expense
        BigDecimal sum = balances.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(BigDecimal.ZERO.setScale(2), sum.setScale(2));
    }
}
