package se.hem.spliteven.model;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @NotBlank
    private String name;

    @ManyToMany
    @JoinTable(name = "account_person", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))

    private List<Person> persons = new ArrayList<>();

    protected Account() {

    }

    public Account(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void addPerson(Person person) {
        persons.add(person);
        person.addAccount(this);
    }

    public void removePerson(Person person) {
        persons.remove(person);
        person.removeAccount(this);
    }

    public int numberOfMembers() {
        return persons.size();
    }

    public boolean isAccountMember(Person person) {
        return persons.contains(person);
    }
}
