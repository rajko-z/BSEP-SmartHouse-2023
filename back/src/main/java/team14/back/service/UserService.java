package team14.back.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import team14.back.dto.CSRRequestDTO;
import team14.back.dto.UserDTO;

import java.io.IOException;
import java.util.List;


public interface UserService extends UserDetailsService {
    List<UserDTO> findAll();

    void register(CSRRequestDTO requestDTO, MultipartFile document) throws IOException;
}
