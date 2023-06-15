package team14.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import team14.back.dto.LogDTO;
import team14.back.dto.UserWithTokenDTO;
import team14.back.dto.login.LoginDTO;
import team14.back.dto.login.LoginWith2FACodeDto;
import team14.back.enumerations.LogAction;
import team14.back.service.alarm.AlarmService;
import team14.back.service.auth.AuthenticationService;
import team14.back.service.log.LogService;
import team14.back.utils.HttpUtils;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private static final String CLS_NAME = AuthenticationController.class.getName();

    private final AuthenticationService authenticationService;

    private final LogService logService;

    private final AlarmService alarmService;

    @PostMapping("/login-final-step")
    public ResponseEntity<UserWithTokenDTO> createAuthenticationToken(@RequestBody @Valid LoginWith2FACodeDto loginDTO,
                                                                      HttpServletRequest request) {
        UserWithTokenDTO userWithToken = authenticationService.createAuthenticationToken(loginDTO, request);

        String cookie = "Fingerprint=" + userWithToken.getFingerprint() + "; HttpOnly; Path=/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", cookie);

        logService.addInfo(new LogDTO(LogAction.LOG_IN_SUCCESS, CLS_NAME,
                "Success login for user: " + loginDTO.getEmail(), HttpUtils.getRequestIP(request)));
        return ResponseEntity.ok().headers(headers).body(userWithToken);
    }

    @PostMapping("/login-first-step")
    public ResponseEntity<Map<String,Boolean>> checkCredentials(@RequestBody @Valid LoginDTO loginDTO,
                                                                HttpServletRequest request) {
        boolean credentialsValid = authenticationService.firstLoginStep(loginDTO, request);
        Map<String, Boolean> response = new HashMap<>();
        response.put("credentialsValid", credentialsValid);

        if (!credentialsValid) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
