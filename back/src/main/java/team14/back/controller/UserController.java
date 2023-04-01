package team14.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team14.back.dto.CSRRequestDTO;
import team14.back.dto.UserDTO;
import team14.back.service.UserService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    /// ovo je samo probna metoda da bi testirao security
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OWNER')")
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping(path = "/register", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> register(@RequestPart("request") CSRRequestDTO requestDTO, @RequestPart("file") MultipartFile document) {
        try {
            userService.register(requestDTO, document);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}
