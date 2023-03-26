package team14.back.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import team14.back.dto.UserDTO;

import java.util.List;


public interface UserService extends UserDetailsService {
    List<UserDTO> findAll();
}
