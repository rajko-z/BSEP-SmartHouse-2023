package team14.back.service.email;

import team14.back.dto.LoginDTO;

public interface EmailService {

    void sendCSRRejectionEmail(String email, String reason);

    void sendCreatedCertificateAndPasswordToUser(LoginDTO createdCredentials);

    void sendBlockingUserEmail(String email);
}
