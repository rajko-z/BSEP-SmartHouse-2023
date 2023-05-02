package team14.back.service.email;

import team14.back.dto.login.LoginDTO;

public interface EmailService {

    void sendCSRRejectionEmail(String email, String reason);

    void sendCreatedCertificateAndPasswordToUser(LoginDTO createdCredentials);

    void sendBlockingUserEmail(String email);

    void sendMFACodeToUser(String email, String code);
}
