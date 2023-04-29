package team14.back.service.auth.mfa;

public interface MfaService {

    void createAndSendCodeToUser(String email);

    boolean isCodeValidForUser(String email, String mfaCode);
}
