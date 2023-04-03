package team14.back.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import team14.back.dto.csr.CSRRequestDTO;
import team14.back.dto.LoginDTO;

import java.io.IOException;


public interface UserService extends UserDetailsService {

    void register(CSRRequestDTO requestDTO, MultipartFile document) throws IOException;

    LoginDTO createNewUser(String email);
}
