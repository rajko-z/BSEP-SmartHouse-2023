package team14.back.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import team14.back.dto.*;
import team14.back.dto.csr.CSRRequestDTO;
import team14.back.dto.login.LoginDTO;
import team14.back.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface UserService extends UserDetailsService {

    void register(CSRRequestDTO requestDTO, MultipartFile document) throws IOException;

    LoginDTO createNewUser(String email);

    void blockUser(String email);

    void unblockUser(String email);

    Optional<User> findUserByEmail(String email);

    void save(User user);

    void changePassword(NewPasswordDTO newPasswordDTO);

    void addUser(AddUserDTO addUserDTO);

    List<User> getAllUsers();

    void changeUserRole(ChangeRoleDto changeRoleDto);

    void deleteUser(String userEmail);

    void undeleteUser(String userEmail);

    User getUserByEmail(String email);

    List<String> getAllNonAdminEmails();

    void saveFacilities(UserFacilitiesDTO userFacilitiesDTO);

    List<FacilityDTO> getUserFacilities(String email);

    void generateFile(String configFilePath) throws IOException;

    void deleteFile(String filePath) throws IOException;
}
