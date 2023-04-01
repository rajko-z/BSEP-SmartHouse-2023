package team14.back.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import team14.back.dto.CSRRequestDTO;

import java.io.IOException;


public interface UserService extends UserDetailsService {

    void register(CSRRequestDTO requestDTO, MultipartFile document) throws IOException;
}
