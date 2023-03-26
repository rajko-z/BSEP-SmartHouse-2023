package team14.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import team14.back.dto.LoginDTO;
import team14.back.dto.UserWithTokenDTO;
import team14.back.service.AuthenticationService;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<UserWithTokenDTO> createAuthenticationToken(@RequestBody LoginDTO loginDTO) {
        UserWithTokenDTO userWithToken = authenticationService.createAuthenticationToken(loginDTO);
        return new ResponseEntity<>(userWithToken, HttpStatus.OK);
    }
}
