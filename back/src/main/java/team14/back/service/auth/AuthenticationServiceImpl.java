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
import team14.back.dto.LogDTO;
import team14.back.dto.UserWithTokenDTO;
import team14.back.dto.login.LoginDTO;
import team14.back.dto.login.LoginWith2FACodeDto;
import team14.back.enumerations.LogAction;
import team14.back.exception.InvalidCredentialsException;
import team14.back.model.User;
import team14.back.service.alarm.AlarmService;
import team14.back.service.auth.loginfailure.LoginFailureService;
import team14.back.service.auth.mfa.MfaService;
import team14.back.service.email.EmailService;
import team14.back.service.log.LogService;
import team14.back.service.user.UserService;
import team14.back.utils.ExceptionMessageConstants;
import team14.back.utils.HttpUtils;
import team14.back.utils.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String CLS_NAME = AuthenticationServiceImpl.class.getName();

    private final TokenUtils tokenUtils;

    private final AuthenticationManager authenticationManager;

    private final LoginFailureService loginFailureService;

    private final UserService userService;

    private final MfaService mfaService;

    private final EmailService emailService;

    private final LogService logService;

    @Transactional
    public UserWithTokenDTO createAuthenticationToken(LoginWith2FACodeDto loginDTO, HttpServletRequest request) {
        try
        {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

            checkForMFACode((User) authentication.getPrincipal(), loginDTO.getEmail(), loginDTO.getMfaCode(), request);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();
            String fingerprint = tokenUtils.generateFingerprint();
            String jwt = tokenUtils.generateToken(user.getUsername(), fingerprint);

            return UserDTOConverter.convertToUserWithToken(user, jwt, fingerprint);
        }
        catch (BadCredentialsException ex) {
            logService.addErr(new LogDTO(LogAction.INVALID_CREDENTIALS, CLS_NAME, "Invalid login for user: " + loginDTO.getEmail(), HttpUtils.getRequestIP(request)));
            throw new InvalidCredentialsException(ExceptionMessageConstants.INVALID_LOGIN);
        }
    }

    private void checkForMFACode(User user, String email, String code, HttpServletRequest request) {
        boolean isCodeValid = mfaService.isCodeValidForUser(email, code);

        if (!isCodeValid) {
            boolean blocked = increaseLoginFailureAndBlockUserIfNeeded(email, request);
            if (blocked) {
                emailService.sendBlockingUserEmail(email);
                logService.addInfo(new LogDTO(LogAction.SENDING_BLOCKING_USER_EMAIL, CLS_NAME, "3 times login failure. Blocking user: " + email, HttpUtils.getRequestIP(request)));
            }
            user.setMfaCode(null);
            user.setMfaCodeTimestamp(null);
            userService.save(user);
            logService.addErr(new LogDTO(LogAction.INVALID_2FA, CLS_NAME, "Invalid 2fa for user: " + email, HttpUtils.getRequestIP(request)));
            throw new InvalidCredentialsException(ExceptionMessageConstants.INVALID_TWO_FACTOR);
        }
    }

    @Override
    public boolean firstLoginStep(LoginDTO loginDTO, HttpServletRequest request) {
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
            boolean blocked = increaseLoginFailureAndBlockUserIfNeeded(loginDTO.getEmail(), request);
            if (blocked) {
                emailService.sendBlockingUserEmail(loginDTO.getEmail());
                logService.addInfo(new LogDTO(LogAction.SENDING_BLOCKING_USER_EMAIL, CLS_NAME, "3 times login failure. Blocking user: " + loginDTO.getEmail(), HttpUtils.getRequestIP(request)));
            }
            logService.addErr(new LogDTO(LogAction.INVALID_CREDENTIALS, CLS_NAME, "Invalid credentials for: " + loginDTO.getEmail(), HttpUtils.getRequestIP(request)));
            return false;
        }
    }

    @Override
    public boolean increaseLoginFailureAndBlockUserIfNeeded(String email, HttpServletRequest request) {
        Optional<User> user = this.userService.findUserByEmail(email);
        if (user.isEmpty() || user.get().isBlocked()) {
            return false;
        }

        int loginFailures = this.loginFailureService.findNumberOfLoginFailuresInLast10Minutes(email);
        this.loginFailureService.addNewLoginFailureForUser(email);

        if (loginFailures >= 2) {
            userService.blockUser(email, request);
            return true;
        }
        return false;
    }

}
