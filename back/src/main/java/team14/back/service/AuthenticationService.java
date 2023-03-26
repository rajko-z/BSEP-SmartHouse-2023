package team14.back.service;

import team14.back.dto.LoginDTO;
import team14.back.dto.UserWithTokenDTO;

public interface AuthenticationService {
    UserWithTokenDTO createAuthenticationToken(LoginDTO loginDTO);
}
