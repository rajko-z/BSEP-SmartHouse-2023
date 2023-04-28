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
import team14.back.service.auth.AuthenticationService;
import team14.back.service.email.EmailService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<UserWithTokenDTO> createAuthenticationToken(@RequestBody LoginDTO loginDTO) {
        UserWithTokenDTO userWithToken = authenticationService.createAuthenticationToken(loginDTO);
        return new ResponseEntity<>(userWithToken, HttpStatus.OK);
    }

    @PostMapping("/login-first-step")
    public ResponseEntity<Map<String,Boolean>> checkCredentials(@RequestBody LoginDTO loginDTO) {
        boolean credentialsValid = authenticationService.areCredentialsValid(loginDTO);
        if (!credentialsValid) {
            boolean blocked = authenticationService.increaseLoginFailureAndBlockUserIfNeeded(loginDTO.getEmail());
            if (blocked) {
                emailService.sendBlockingUserEmail(loginDTO.getEmail());
            }
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("credentialsValid", credentialsValid);

        if (credentialsValid) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
