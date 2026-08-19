package se.hem.spliteven.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// import jakarta.annotation.PostConstruct;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;
    /*
     * @Value("${spring.mail.password}")
     * private String debugPassword;
     */

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    /*
     * @PostConstruct
     * public void debugPrintCredentials() {
     * System.out.println("=== MAIL DEBUG ===");
     * System.out.println("Username: [" + fromAddress + "]");
     * System.out.println("Password length: " + (debugPassword != null ?
     * debugPassword.length() : "NULL"));
     * System.out.println("==================");
     * }
     */

    public void sendMembershipInvite(String toEmail, String accountName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("Du har blivit inbjuden till " + accountName + " på SplitEven");
        message.setText("""
                Hej!

                Du har blivit inbjuden att gå med i kontot "%s" på SplitEven.

                Logga in på SplitEven för att godkänna eller neka inbjudan.

                Hälsningar,
                SplitEven
                """.formatted(accountName));

        mailSender.send(message);
    }
}
