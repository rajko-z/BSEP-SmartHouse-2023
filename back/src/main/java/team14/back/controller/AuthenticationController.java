package team14.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import team14.back.dto.UserWithTokenDTO;
import team14.back.dto.login.LoginDTO;
import team14.back.dto.login.LoginWith2FACodeDto;
import team14.back.service.auth.AuthenticationService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login-final-step")
    public ResponseEntity<UserWithTokenDTO> createAuthenticationToken(@RequestBody @Valid LoginWith2FACodeDto loginDTO) {
        UserWithTokenDTO userWithToken = authenticationService.createAuthenticationToken(loginDTO);
        return new ResponseEntity<>(userWithToken, HttpStatus.OK);
    }

    @PostMapping("/login-first-step")
    public ResponseEntity<Map<String,Boolean>> checkCredentials(@RequestBody @Valid LoginDTO loginDTO) {
        boolean credentialsValid = authenticationService.firstLoginStep(loginDTO);
        Map<String, Boolean> response = new HashMap<>();
        response.put("credentialsValid", credentialsValid);

        if (!credentialsValid) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
