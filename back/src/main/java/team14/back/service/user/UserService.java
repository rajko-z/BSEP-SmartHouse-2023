package team14.back.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import team14.back.dto.*;
import team14.back.dto.csr.CSRRequestDTO;
import team14.back.dto.login.LoginDTO;
import team14.back.model.User;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface UserService extends UserDetailsService {

    void register(CSRRequestDTO requestDTO, MultipartFile document, HttpServletRequest request) throws IOException;

    LoginDTO createNewUser(String email, HttpServletRequest request);

    void blockUser(String email, HttpServletRequest request);

    void unblockUser(String email, HttpServletRequest request);

    Optional<User> findUserByEmail(String email);

    void save(User user);

    void changePassword(NewPasswordDTO newPasswordDTO, HttpServletRequest request);

    void addUser(AddUserDTO addUserDTO);

    List<User> getAllUsers(HttpServletRequest request);

    void changeUserRole(ChangeRoleDto changeRoleDto, HttpServletRequest request);

    void deleteUser(String userEmail, HttpServletRequest request);

    void undeleteUser(String userEmail, HttpServletRequest request);

    User getUserByEmail(String email);

    List<String> getAllNonAdminEmails();

    void saveFacilities(UserFacilitiesDTO userFacilitiesDTO, HttpServletRequest request);

    List<FacilityDTO> getUserFacilities(String email, HttpServletRequest request);

    void generateFile(String configFilePath) throws IOException;

    void deleteFile(String filePath) throws IOException;
}
