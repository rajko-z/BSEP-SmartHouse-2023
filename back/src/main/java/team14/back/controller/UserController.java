package team14.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team14.back.dto.*;
import team14.back.dto.csr.CSRRequestDTO;
import team14.back.model.User;
import team14.back.exception.BadRequestException;
import team14.back.exception.NotFoundException;
import team14.back.service.alarm.AlarmService;
import team14.back.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
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
    private final AlarmService alarmService;

    @PostMapping(path = "/register", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> register(@RequestPart("request") CSRRequestDTO requestDTO,
                                      @RequestPart("file") MultipartFile document,
                                      HttpServletRequest request) {
        try {
            userService.register(requestDTO, document, request);
            alarmService.checkForRedundantCSRRequest();
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
    public ResponseEntity<TextResponse> changePassword(@RequestBody @Valid NewPasswordDTO newPasswordDTO, HttpServletRequest request) {
        userService.changePassword(newPasswordDTO, request);
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
    public ResponseEntity<?> getAllUsers(HttpServletRequest request){
        List<User> users = userService.getAllUsers(request);
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/change-user-role")
    public ResponseEntity<?> changeUserRole(@RequestBody @Valid ChangeRoleDto changeRoleDto, HttpServletRequest request){
        this.userService.changeUserRole(changeRoleDto, request);
        return new ResponseEntity<>(new TextResponse("Role successfully changed"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestParam @Email String userEmail, HttpServletRequest request){
        this.userService.deleteUser(userEmail, request);
        return new ResponseEntity<>(new TextResponse("User successfully deleted"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/undelete-user")
    public ResponseEntity<?> undeleteUser(@RequestParam  @Email String userEmail, HttpServletRequest request){
        this.userService.undeleteUser(userEmail, request);
        return new ResponseEntity<>(new TextResponse("User successfully undeleted"), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/block-user")
    public ResponseEntity<?> blockUser(@RequestParam @Email String userEmail, HttpServletRequest request){
        this.userService.blockUser(userEmail, request);
        return new ResponseEntity<>(new TextResponse("User successfully blocked"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/unblock-user")
    public ResponseEntity<?> unblockUser(@RequestParam @Email String userEmail, HttpServletRequest request){
        this.userService.unblockUser(userEmail, request);
        return new ResponseEntity<>(new TextResponse("User successfully unblocked"), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OWNER')")
    @GetMapping("/{email}")
    public ResponseEntity<?> getUser(@PathVariable @Email String email){
        User user = this.userService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save-facilities")
    public ResponseEntity<?> saveFacilities(@RequestBody @Valid UserFacilitiesDTO userFacilitiesDTO, HttpServletRequest request){
        this.userService.saveFacilities(userFacilitiesDTO, request);
        return new ResponseEntity<>(new TextResponse("Users facilities successfully saved"), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OWNER')")
    @GetMapping("/facilities/{email}")
    public ResponseEntity<?> getUserFacilities(@PathVariable @Email String email, HttpServletRequest request){
        List<FacilityDTO> facilities = this.userService.getUserFacilities(email, request);
        return new ResponseEntity<>(facilities, HttpStatus.OK);
    }

}
