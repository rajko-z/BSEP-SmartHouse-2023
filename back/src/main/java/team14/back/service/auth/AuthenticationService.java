package team14.back.service.auth;

import team14.back.dto.login.LoginDTO;
import team14.back.dto.UserWithTokenDTO;
import team14.back.dto.login.LoginWith2FACodeDto;

public interface AuthenticationService {
    UserWithTokenDTO createAuthenticationToken(LoginWith2FACodeDto loginDTO);

    boolean firstLoginStep(LoginDTO loginDTO);

    /**
     * @param email email of user that failed to log in
     * @return information if user is blocked after 3 login failures
     * **/
    boolean increaseLoginFailureAndBlockUserIfNeeded(String email);
}
