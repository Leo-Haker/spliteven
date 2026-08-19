package se.hem.spliteven.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
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

}
