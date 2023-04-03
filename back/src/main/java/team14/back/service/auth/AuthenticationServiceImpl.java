package team14.back.service.auth;

import lombok.AllArgsConstructor;
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
import team14.back.utils.ExceptionMessageConstants;
import team14.back.utils.TokenUtils;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TokenUtils tokenUtils;

    private final AuthenticationManager authenticationManager;

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

    private void checkIfUserIsDeleted(User user){
        if(user.isDeleted())
            throw new InvalidCredentialsException(ExceptionMessageConstants.INVALID_LOGIN);
    }
}
