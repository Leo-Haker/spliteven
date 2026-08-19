package se.hem.spliteven.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import se.hem.spliteven.dto.CreatePersonRequest;
import se.hem.spliteven.model.Person;
import se.hem.spliteven.repository.PersonRepository;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonService(
            PersonRepository personRepository,
            PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Person createPerson(CreatePersonRequest request) {
        String passwordHash = passwordEncoder.encode(request.password());

        Person person = new Person(request.name(), request.email(), passwordHash);

        return personRepository.save(person);
    }

    public Person login(String email, String password) throws IllegalArgumentException {
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Fel e-post eller lösenord"));

        if (!passwordEncoder.matches(password, person.getPasswordHash())) {
            throw new IllegalArgumentException("Fel e-post eller lösenord");
        }

        return person;
    }

}
