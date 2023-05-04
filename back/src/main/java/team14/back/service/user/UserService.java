package team14.back.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import team14.back.dto.AddUserDTO;
import team14.back.dto.NewPasswordDTO;
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

    Optional<User> findUserByEmail(String email);

    void save(User user);

    void changePassword(NewPasswordDTO newPasswordDTO);

    void addUser(AddUserDTO addUserDTO);

    List<String> getAllNonAdminEmails();
}
