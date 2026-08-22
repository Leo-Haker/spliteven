package se.hem.spliteven.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @NotNull
    @ManyToOne
    private Account account;

    @NotNull
    @ManyToOne
    private Person paidBy;

    @NotBlank
    private String description;

    private boolean income; // denote if it is an expense or income

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    @Min(0)
    @Max(100)
    private int ownSharePercentage;

    @NotNull
    private LocalDate date;

    protected Expense() {
    }

    public Expense(Account account, Person paidBy, boolean income,
            String description, BigDecimal amount, LocalDate date) {
        this.amount = amount;
        this.paidBy = paidBy;
        this.description = description;
        this.account = account;
        this.ownSharePercentage = 100 / account.numberOfMembers();
        this.date = date;
        this.income = income;
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Person getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(Person paidBy) {
        this.paidBy = paidBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getOwnAmount() {
        // The payer's own share is excluded from what they receive or owe.
        BigDecimal percetage = BigDecimal.valueOf(ownSharePercentage).divide(BigDecimal.valueOf(100), 4,
                RoundingMode.HALF_UP);
        BigDecimal ownShare = amount.multiply(percetage);
        BigDecimal result = amount.subtract(ownShare);
        return income ? result.negate() : result;
    }

    public int getOwnSharePercentage() {
        return ownSharePercentage;
    }

    public void setOwnSharePercentage(int newPercentage) {
        this.ownSharePercentage = newPercentage;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isIncome() {
        return income;
    }

    public BigDecimal getSharePerOtherMember() {
        // Distribute the share not paid by the payer evenly among all other members.
        BigDecimal numberOfOtherPersons = BigDecimal.valueOf(account.numberOfMembers() - 1);
        BigDecimal otherShare = BigDecimal.valueOf(100 - ownSharePercentage)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                .divide(numberOfOtherPersons, 4, RoundingMode.HALF_UP);

        BigDecimal result = amount.multiply(otherShare);
        if (income)
            return result;
        else
            return result.negate();
    }

}
