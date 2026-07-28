package se.hem.spliteven.controller;

import se.hem.spliteven.dto.PersonDto;
import se.hem.spliteven.model.Person;
import se.hem.spliteven.repository.PersonRepository;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @PostMapping
    public PersonDto create(@Valid @RequestBody Person person) {
        Person saved = personRepository.save(person);
        return toDto(saved);
    }

    @GetMapping
    public List<PersonDto> getAll() {
        return personRepository.findAll().stream().map(this::toDto).toList();
    }

    private PersonDto toDto(Person p) {
        return new PersonDto(p.getId(), p.getName(), p.getEmail());
    }
}
