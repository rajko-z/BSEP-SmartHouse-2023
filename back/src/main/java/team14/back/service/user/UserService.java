package team14.back.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import team14.back.dto.csr.CSRRequestDTO;
import team14.back.dto.LoginDTO;
import team14.back.model.User;

import java.io.IOException;
import java.util.Optional;


public interface UserService extends UserDetailsService {

    void register(CSRRequestDTO requestDTO, MultipartFile document) throws IOException;

    LoginDTO createNewUser(String email);

    void blockUser(String email);

    Optional<User> findUserByEmail(String email);
}