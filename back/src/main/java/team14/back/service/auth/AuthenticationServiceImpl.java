package team14.back.service.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team14.back.converters.UserDTOConverter;
import team14.back.dto.UserWithTokenDTO;
import team14.back.dto.login.LoginDTO;
import team14.back.dto.login.LoginWith2FACodeDto;
import team14.back.exception.InvalidCredentialsException;
import team14.back.model.User;
import team14.back.service.auth.loginfailure.LoginFailureService;
import team14.back.service.auth.mfa.MfaService;
import team14.back.service.email.EmailService;
import team14.back.service.user.UserService;
import team14.back.utils.ExceptionMessageConstants;
import team14.back.utils.TokenUtils;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TokenUtils tokenUtils;

    private final AuthenticationManager authenticationManager;

    private final LoginFailureService loginFailureService;

    private final UserService userService;

    private final MfaService mfaService;

    private final EmailService emailService;

    @Transactional
    public UserWithTokenDTO createAuthenticationToken(LoginWith2FACodeDto loginDTO) {
        try
        {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

            checkForMFACode((User) authentication.getPrincipal(), loginDTO.getEmail(), loginDTO.getMfaCode());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();
            String fingerprint = tokenUtils.generateFingerprint();
            String jwt = tokenUtils.generateToken(user.getUsername(), fingerprint);

            return UserDTOConverter.convertToUserWithToken(user, jwt, fingerprint);
        }
        catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException(ExceptionMessageConstants.INVALID_LOGIN);
        }
    }

    private void checkForMFACode(User user, String email, String code) {
        boolean isCodeValid = mfaService.isCodeValidForUser(email, code);

        if (!isCodeValid) {
            boolean blocked = increaseLoginFailureAndBlockUserIfNeeded(email);
            if (blocked) {
                emailService.sendBlockingUserEmail(email);
            }
            user.setMfaCode(null);
            user.setMfaCodeTimestamp(null);
            userService.save(user);

            throw new InvalidCredentialsException(ExceptionMessageConstants.INVALID_TWO_FACTOR);
        }
    }

    @Override
    public boolean firstLoginStep(LoginDTO loginDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword())
            );
            mfaService.createAndSendCodeToUser(loginDTO.getEmail());
            return true;
        }
        catch (BadCredentialsException | AccountStatusException ex)
        {
            boolean blocked = increaseLoginFailureAndBlockUserIfNeeded(loginDTO.getEmail());
            if (blocked) {
                emailService.sendBlockingUserEmail(loginDTO.getEmail());
            }
            return false;
        }
    }

    @Override
    public boolean increaseLoginFailureAndBlockUserIfNeeded(String email) {
        Optional<User> user = this.userService.findUserByEmail(email);
        if (user.isEmpty() || user.get().isBlocked()) {
            return false;
        }

        int loginFailures = this.loginFailureService.findNumberOfLoginFailuresInLast10Minutes(email);
        this.loginFailureService.addNewLoginFailureForUser(email);

        if (loginFailures >= 2) {
            userService.blockUser(email);
            return true;
        }
        return false;
    }

}
