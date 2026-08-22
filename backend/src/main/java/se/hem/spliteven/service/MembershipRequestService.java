package se.hem.spliteven.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import se.hem.spliteven.model.Account;
import se.hem.spliteven.model.MembershipRequest;
import se.hem.spliteven.model.Person;
import se.hem.spliteven.repository.AccountRepository;
import se.hem.spliteven.repository.ExpenseRepository;
import se.hem.spliteven.repository.MembershipRequestRepository;
import se.hem.spliteven.repository.PersonRepository;

@Service
public class MembershipRequestService extends AbstractService {

    private final JavaMailSender mailSender;
    private final MembershipRequestRepository membershipRequestRepository;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public MembershipRequestService(
            JavaMailSender mailSender,
            MembershipRequestRepository membershipRequestRepository,
            ExpenseRepository expenseRepository,
            AccountRepository accountRepository,
            PersonRepository personRepository) {
        super(expenseRepository, accountRepository, personRepository);
        this.mailSender = mailSender;
        this.membershipRequestRepository = membershipRequestRepository;

    }

    public void sendMembershipInvite(String toEmail, String accountName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject(invitationSubject(accountName));
        message.setText(invitationText(accountName));

        mailSender.send(message);
    }

    private String invitationSubject(String accountName) {
        return "Du har blivit inbjuden till " + accountName + " på SplitEven";
    }

    private String invitationText(String accountName) {
        return """
                Hej!

                Du har blivit inbjuden att gå med i kontot "%s" på SplitEven.

                Logga in på SplitEven för att godkänna eller neka inbjudan.

                Hälsningar,
                SplitEven
                """.formatted(accountName);
    }

    public MembershipRequest create(Long accountId, String email) {
        Account account = findAccountById(accountId);
        Person person = findPersonByEmail(email);
        MembershipRequest membershipRequest = new MembershipRequest(account, person);
        try {
            sendMembershipInvite(email, account.getName());
        } catch (Exception e) {
            // Persist the invitation even when the mail provider is temporarily
            // unavailable.
            System.err.println("Kunde inte skicka e-post: " + e.getMessage());
        }
        return membershipRequestRepository.save(membershipRequest);
    }

    public List<MembershipRequest> getPending(Long personId) {
        return membershipRequestRepository.findByPersonIdAndStatus(personId, MembershipRequest.Status.PENDING);
    }

    public void accept(Long requestId) {
        MembershipRequest request = findRequestById(requestId);
        Account account = request.getAccount();
        Person person = request.getPerson();
        // Update the account before marking the request accepted so both states stay
        // consistent.
        account.addPerson(person);
        accountRepository.save(account);

        request.setStatus(MembershipRequest.Status.ACCEPTED);
        membershipRequestRepository.save(request);

    }

    public void decline(Long requestId) {
        MembershipRequest request = findRequestById(requestId);
        request.setStatus(MembershipRequest.Status.DECLINED);
        membershipRequestRepository.save(request);

    }

    private MembershipRequest findRequestById(Long requestId) {
        return membershipRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Inga förfrågor hittades"));
    }

}
