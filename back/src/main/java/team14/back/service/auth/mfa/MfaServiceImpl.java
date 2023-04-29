package team14.back.service.auth.mfa;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team14.back.model.User;
import team14.back.service.email.EmailService;
import team14.back.service.user.UserService;

import java.security.SecureRandom;

@Service
@AllArgsConstructor
public class MfaServiceImpl implements MfaService{

    private static final int MFA_CODE_LENGTH = 6;

    private static final int MFA_MAX_TIME_TO_LIVE_IN_SECONDS = 120;

    private final UserService userService;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void createAndSendCodeToUser(String email) {
        User user = userService.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Can't find user with email: " + email));
        String code = createCode();
        user.setMfaCode(passwordEncoder.encode(code));
        user.setMfaCodeTimestamp(System.currentTimeMillis());
        userService.save(user);
        emailService.sendMFACodeToUser(email, code);
    }

    @Override
    public boolean isCodeValidForUser(String email, String mfaCode) {
        User user = userService.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Can't find user with email: " + email));
        Long timestamp = user.getMfaCodeTimestamp();
        String encodedCode = user.getMfaCode();

        return timestamp != null &&
               encodedCode != null &&
               ((System.currentTimeMillis() - timestamp) / 1000) < MFA_MAX_TIME_TO_LIVE_IN_SECONDS &&
               passwordEncoder.matches(mfaCode, encodedCode);
    }

    private String createCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(MFA_CODE_LENGTH);
        for (int i = 0; i < MFA_CODE_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
