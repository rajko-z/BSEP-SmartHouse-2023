package team14.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team14.back.dto.AddUserDTO;
import team14.back.dto.ChangeRoleDto;
import team14.back.dto.NewPasswordDTO;
import team14.back.dto.TextResponse;
import team14.back.dto.csr.CSRRequestDTO;
import team14.back.model.User;
import team14.back.exception.BadRequestException;
import team14.back.exception.NotFoundException;
import team14.back.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.websocket.server.PathParam;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/change-user-role")
    public ResponseEntity<?> changeUserRole(@RequestBody @Valid ChangeRoleDto changeRoleDto){
        this.userService.changeUserRole(changeRoleDto);
        return new ResponseEntity<>(new TextResponse("Role successfully changed"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestParam @Email String userEmail){
        this.userService.deleteUser(userEmail);
        return new ResponseEntity<>(new TextResponse("User successfully deleted"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/undelete-user")
    public ResponseEntity<?> undeleteUser(@RequestParam  @Email String userEmail){
        this.userService.undeleteUser(userEmail);
        return new ResponseEntity<>(new TextResponse("User successfully undeleted"), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/block-user")
    public ResponseEntity<?> blockUser(@RequestParam @Email String userEmail){
        this.userService.blockUser(userEmail);
        return new ResponseEntity<>(new TextResponse("User successfully blocked"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/unblock-user")
    public ResponseEntity<?> unblockUser(@RequestParam @Email String userEmail){
        this.userService.unblockUser(userEmail);
        return new ResponseEntity<>(new TextResponse("User successfully unblocked"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{email}")
    public ResponseEntity<?> getUser(@PathVariable @Email String email){
        User user = this.userService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
