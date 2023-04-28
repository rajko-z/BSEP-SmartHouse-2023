package team14.back.service.auth;

import team14.back.dto.LoginDTO;
import team14.back.dto.UserWithTokenDTO;

public interface AuthenticationService {
    UserWithTokenDTO createAuthenticationToken(LoginDTO loginDTO);

    boolean areCredentialsValid(LoginDTO loginDTO);

    /**
     * @param email email of user that failed to log in
     * @return information if user is blocked after 3 login failures
     * **/
    boolean increaseLoginFailureAndBlockUserIfNeeded(String email);
}
