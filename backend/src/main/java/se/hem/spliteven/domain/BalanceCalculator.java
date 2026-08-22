package se.hem.spliteven.domain;

import se.hem.spliteven.model.Account;
import se.hem.spliteven.model.Expense;
import se.hem.spliteven.model.Person;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Calulates the balance between the account memebers
 * base on a list of expenses
 * 
 */
public class BalanceCalculator {
    private final Account account;

    public BalanceCalculator(Account account) {
        this.account = account;
    }

    public Map<Person, BigDecimal> calculateBalances(List<Expense> expenses, YearMonth from, YearMonth to) {
        Map<Person, BigDecimal> balances = new HashMap<>();

        for (Person person : account.getPersons()) {
            balances.put(person, BigDecimal.ZERO);
        }

        for (Expense expense : expenses) {
            if (!expense.getAccount().equals(account)) {
                throw new IllegalStateException("Expense does not belong to this account.");
            }

            if (!inRange(expense, from, to)) {
                continue;
            }

            Person paidBy = expense.getPaidBy();

            if (!account.isAccountMember(paidBy)) {
                throw new IllegalStateException("Expense payer is not a member of the account.");
            }

            // Positive means the member should receive money; negative means they owe
            // money.
            addToBalance(balances, paidBy, expense.getOwnAmount());

            for (Person person : account.getPersons()) {
                if (!person.equals(paidBy)) {
                    addToBalance(balances, person, expense.getSharePerOtherMember());
                }
            }
        }
        return Map.copyOf(balances);
    }

    private boolean inRange(Expense expense, YearMonth from, YearMonth to) {
        YearMonth expenseMonth = YearMonth.from(expense.getDate());
        return !expenseMonth.isBefore(from) && !expenseMonth.isAfter(to);
    }

    // Helper method: calculate one person's total account balance for the balance
    // overview.
    public BigDecimal calculateBalanceForPerson(Person person, List<Expense> expenses, YearMonth from, YearMonth to) {
        Map<Person, BigDecimal> balances = calculateBalances(expenses, from, to);
        return balances.getOrDefault(person, BigDecimal.ZERO);
    }

    private void addToBalance(Map<Person, BigDecimal> balances, Person person, BigDecimal amount) {
        balances.merge(person, amount, BigDecimal::add);
    }

}
