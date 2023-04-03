package team14.back.service.auth;

import team14.back.dto.LoginDTO;
import team14.back.dto.UserWithTokenDTO;

public interface AuthenticationService {
    UserWithTokenDTO createAuthenticationToken(LoginDTO loginDTO);
}
