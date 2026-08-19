package se.hem.spliteven.controller;

import se.hem.spliteven.dto.CreateMembershipRequest;
import se.hem.spliteven.dto.MembershipRequestDto;
import se.hem.spliteven.model.Account;
import se.hem.spliteven.model.Person;
import se.hem.spliteven.model.MembershipRequest;
import se.hem.spliteven.repository.AccountRepository;
import se.hem.spliteven.repository.MembershipRequestRepository;
import se.hem.spliteven.repository.PersonRepository;
import se.hem.spliteven.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MembershipRequestController {
    private final MembershipRequestRepository requestRepository;
    private final AccountRepository accountRepository;
    private final PersonRepository personRepository;
    private final EmailService emailService;

    public MembershipRequestController(MembershipRequestRepository requestRepository,
            AccountRepository accountRepository,
            PersonRepository personRepository,
            EmailService emailService) {
        this.requestRepository = requestRepository;
        this.accountRepository = accountRepository;
        this.personRepository = personRepository;
        this.emailService = emailService;
    }

    private void sendEmail(String email, String accountName) {
        try {
            emailService.sendMembershipInvite(email, accountName);
        } catch (Exception e) {
            System.err.println("Kunde inte skicka e-post: " + e.getMessage());
        }
    }

    // Create a request: the account invites another person to join by email
    @PostMapping("/api/accounts/{accountId}/requests")
    public ResponseEntity<MembershipRequestDto> create(@PathVariable Long accountId,
            @RequestBody CreateMembershipRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Konto hittades inte"));

        Person person = personRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Ingen användare med den mailadressen"));

        MembershipRequest membershipRequest = new MembershipRequest(account, person);
        MembershipRequest saved = requestRepository.save(membershipRequest);

        MembershipRequestDto dto = new MembershipRequestDto(
                saved.getId(), saved.getAccount().getId(), saved.getAccount().getName(), saved.getStatus().name());

        sendEmail(person.getEmail(), account.getName());

        return ResponseEntity.ok(dto);
    }

    // Get a specific persons pending requests
    @GetMapping("/api/requests/{personId}/requests")
    public List<MembershipRequestDto> getPending(@PathVariable Long personId) {
        return requestRepository.findByPersonIdAndStatus(personId, MembershipRequest.Status.PENDING)
                .stream()
                .map(request -> new MembershipRequestDto(request.getId(), request.getAccount().getId(),
                        request.getAccount().getName(), request.getStatus().name()))
                .toList();
    }

    // A person accepts the invitation to join an account
    @PostMapping("/api/requests/{id}/accept")
    public ResponseEntity<?> accept(@PathVariable Long id) {
        MembershipRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inga förfrågor hittades"));

        request.getAccount().addPerson(request.getPerson());
        accountRepository.save(request.getAccount());

        request.setStatus(MembershipRequest.Status.ACCEPTED);
        requestRepository.save(request);

        return ResponseEntity.ok().build();
    }

    // A person declines the invitation to join an account
    @PostMapping("/api/requests/{id}/decline")
    public ResponseEntity<?> decline(@PathVariable Long id) {
        MembershipRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inga förfrågor hittades"));

        request.setStatus(MembershipRequest.Status.DECLINED);
        requestRepository.save(request);

        return ResponseEntity.ok().build();
    }

}
