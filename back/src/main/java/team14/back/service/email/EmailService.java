package team14.back.service.email;

import team14.back.dto.login.LoginDTO;

import javax.servlet.http.HttpServletRequest;

public interface EmailService {

    void sendCSRRejectionEmail(String email, String reason, HttpServletRequest request);

    void sendCreatedCertificateAndPasswordToUser(LoginDTO createdCredentials, HttpServletRequest request);

    void sendBlockingUserEmail(String email);

    void sendMFACodeToUser(String email, String code);
}
