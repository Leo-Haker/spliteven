package se.hem.spliteven.controller;

import se.hem.spliteven.dto.CreateMembershipRequest;
import se.hem.spliteven.dto.MembershipRequestDto;
import se.hem.spliteven.mapper.DtoMapper;
import se.hem.spliteven.model.MembershipRequest;
import se.hem.spliteven.service.MembershipRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MembershipRequestController {

    private final MembershipRequestService membershipRequestService;
    private final DtoMapper dtoMapper;

    public MembershipRequestController(
            MembershipRequestService membershipRequestService,
            DtoMapper dtoMapper) {
        this.membershipRequestService = membershipRequestService;
        this.dtoMapper = dtoMapper;
    }

    // Create a request: the account invites another person to join by email
    @PostMapping("/api/accounts/{accountId}/requests")
    public ResponseEntity<MembershipRequestDto> create(@PathVariable Long accountId,
            @RequestBody CreateMembershipRequest request) {

        MembershipRequest membershipRequest = membershipRequestService.create(accountId, request.email());
        MembershipRequestDto dto = dtoMapper.toDto(membershipRequest);

        return ResponseEntity.ok(dto);
    }

    // Get a specific persons pending requests
    @GetMapping("/api/requests/{personId}/requests")
    public List<MembershipRequestDto> getPending(@PathVariable Long personId) {
        return membershipRequestService.getPending(personId)
                .stream()
                .map(dtoMapper::toDto)
                .toList();
    }

    // A person accepts the invitation to join an account
    @PostMapping("/api/requests/{id}/accept")
    public ResponseEntity<?> accept(@PathVariable Long id) {
        membershipRequestService.accept(id);

        return ResponseEntity.ok().build();
    }

    // A person declines the invitation to join an account
    @PostMapping("/api/requests/{id}/decline")
    public ResponseEntity<?> decline(@PathVariable Long id) {
        membershipRequestService.decline(id);
        return ResponseEntity.ok().build();
    }

}
