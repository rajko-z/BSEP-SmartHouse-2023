package team14.back.service.email;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import team14.back.dto.login.LoginDTO;
import team14.back.exception.InternalServerException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
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

    @Async
    @Override
    public void sendCreatedCertificateAndPasswordToUser(LoginDTO createdCredentials) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(createdCredentials.getEmail());
            helper.setSubject("Your CSR is accepted and processed");
            helper.setText("Login password: " + createdCredentials.getPassword());

            String fileName = createdCredentials.getEmail() + ".crt";
            String filePath = "src/main/resources/data/crt/" + fileName;

            FileSystemResource file = new FileSystemResource(new File(filePath));
            helper.addAttachment(fileName, file);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new InternalServerException("Error happened while sending email to user: " + createdCredentials.getEmail());
        }
    }

    @Async
    @Override
    public void sendBlockingUserEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        message.setSubject("Your account has been blocked");
        message.setText("Someone tried to login with your email 3 times in last 10 minutes.\nIf this was you, contact admin support to unblock your account.\nIf this was not your activity please contact admin as soon as possible.\n\nYour smarthouse team");
        javaMailSender.send(message);
    }

    @Async
    @Override
    public void sendMFACodeToUser(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
        message.setSubject("Your two factor authentication code");
        message.setText("Your code is: " + code);
        javaMailSender.send(message);
    }
}
