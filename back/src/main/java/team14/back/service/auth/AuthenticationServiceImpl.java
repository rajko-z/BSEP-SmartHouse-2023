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
import team14.back.dto.LoginDTO;
import team14.back.dto.UserWithTokenDTO;
import team14.back.exception.InvalidCredentialsException;
import team14.back.model.User;
import team14.back.service.auth.AuthenticationService;
import team14.back.service.auth.loginfailure.LoginFailureService;
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


    // TODO:: replace with code also, remove checkIfUserIsDeleted call
    @Transactional
    public UserWithTokenDTO createAuthenticationToken(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();
            checkIfUserIsDeleted(user);
            String jwt = tokenUtils.generateTokenForUsername(user.getUsername());
            return UserDTOConverter.convertToUserWithToken(user, jwt);
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException(ExceptionMessageConstants.INVALID_LOGIN);
        }
    }

    @Override
    public boolean areCredentialsValid(LoginDTO loginDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword())
            );
            return true;
        } catch (BadCredentialsException | AccountStatusException ex) {
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


    private void checkIfUserIsDeleted(User user){
        if(user.isDeleted())
            throw new InvalidCredentialsException(ExceptionMessageConstants.INVALID_LOGIN);
    }
}
