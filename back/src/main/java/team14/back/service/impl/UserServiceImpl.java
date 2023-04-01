package team14.back.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team14.back.converters.UserDTOConverter;
import team14.back.dto.CSRRequestDTO;
import team14.back.dto.UserDTO;
import team14.back.model.CSRRequest;
import team14.back.model.User;
import team14.back.repository.CSRRequestRepository;
import team14.back.repository.UserRepository;
import team14.back.service.UserService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CSRRequestRepository csrRequestRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent())
            return user.get();
        throw new UsernameNotFoundException("User with username: " + username + " not found");
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(UserDTOConverter::convertBase).toList();
    }

    @Override
    public void register(CSRRequestDTO requestDTO, MultipartFile document) throws IOException {
        CSRRequest csrRequest = new CSRRequest();
        csrRequest.setEmail(requestDTO.getEmail());
        csrRequest.setFirstName(requestDTO.getFirstName());
        csrRequest.setLastName(requestDTO.getLastName());

        String filePath = "src/main/resources/data/csr/"+requestDTO.getEmail()+".csr";
        File path = new File(filePath);
        path.createNewFile();
        FileOutputStream output = new FileOutputStream(path);
        output.write(document.getBytes());
        output.close();

        csrRequest.setFilePath(filePath);
        csrRequestRepository.save(csrRequest);

    }
}
