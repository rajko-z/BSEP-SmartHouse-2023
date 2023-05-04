package team14.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team14.back.dto.AddUserDTO;
import team14.back.dto.NewPasswordDTO;
import team14.back.dto.TextResponse;
import team14.back.dto.csr.CSRRequestDTO;
import team14.back.exception.BadRequestException;
import team14.back.exception.NotFoundException;
import team14.back.service.user.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(path = "/register", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> register(@RequestPart("request") CSRRequestDTO requestDTO, @RequestPart("file") MultipartFile document) {
        try {
            userService.register(requestDTO, document);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("get-all-non-admin-emails")
    public List<String> getAllNonAdminEmails() {
        return userService.getAllNonAdminEmails();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OWNER', 'ROLE_TENANT')")
    @PutMapping(path = "/change-password")
    public ResponseEntity<TextResponse> changePassword(@RequestBody @Valid NewPasswordDTO newPasswordDTO) {
        userService.changePassword(newPasswordDTO);
        return new ResponseEntity<>(new TextResponse("Password successfully changed"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/add-user", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> addUser(@Valid @RequestBody AddUserDTO addUserDTO) {
        try{
            userService.addUser(addUserDTO);
        } catch (BadRequestException e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
