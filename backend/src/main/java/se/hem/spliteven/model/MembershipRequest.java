package se.hem.spliteven.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MembershipRequest {

    public enum Status {
        PENDING, ACCEPTED, DECLINED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Person person;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    protected MembershipRequest() {

    }

    public MembershipRequest(Account account, Person person) {
        this.account = account;
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public Person getPerson() {
        return person;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
