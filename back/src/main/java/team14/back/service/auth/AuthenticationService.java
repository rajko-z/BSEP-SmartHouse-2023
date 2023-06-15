package team14.back.service.auth;

import team14.back.dto.login.LoginDTO;
import team14.back.dto.UserWithTokenDTO;
import team14.back.dto.login.LoginWith2FACodeDto;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    UserWithTokenDTO createAuthenticationToken(LoginWith2FACodeDto loginDTO, HttpServletRequest request);

    boolean firstLoginStep(LoginDTO loginDTO, HttpServletRequest request);

    /**
     * @param email email of user that failed to log in
     * @return information if user is blocked after 3 login failures
     * **/
    boolean increaseLoginFailureAndBlockUserIfNeeded(String email, HttpServletRequest request);
}
