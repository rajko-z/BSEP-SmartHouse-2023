package team14.back.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import team14.back.service.EmailService;

import java.util.Objects;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final Environment env;

    @Override
    @Async
    public void sendCSRRejectionEmail(String toEmail, String reason) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        message.setSubject("Your CSR request has been rejected");

        StringBuilder sb = new StringBuilder("Your csr request has been rejected.");
        if (reason != null && !reason.isBlank()) {
            sb.append("\nReason: ");
            sb.append(reason);
        }
        message.setText(sb.toString());

        javaMailSender.send(message);
    }
}
