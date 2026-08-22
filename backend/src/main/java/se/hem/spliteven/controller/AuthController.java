package se.hem.spliteven.controller;

import org.springframework.web.bind.annotation.*;

import se.hem.spliteven.dto.LoginRequest;
import se.hem.spliteven.dto.PersonDto;
import se.hem.spliteven.mapper.DtoMapper;
import se.hem.spliteven.model.Person;
import se.hem.spliteven.service.PersonService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PersonService personService;
    private final DtoMapper dtoMapper;

    public AuthController(PersonService personService, DtoMapper dtoMapper) {
        this.personService = personService;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping("/login")
    public PersonDto login(@RequestBody LoginRequest request) {
        Person person = personService.login(request.email(), request.password());
        return dtoMapper.toDto(person);
    }

}
