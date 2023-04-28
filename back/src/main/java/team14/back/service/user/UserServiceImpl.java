package team14.back.service.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team14.back.dto.csr.CSRRequestDTO;
import team14.back.dto.LoginDTO;
import team14.back.exception.BadRequestException;
import team14.back.model.CSRRequest;
import team14.back.model.Role;
import team14.back.model.User;
import team14.back.repository.CSRRequestRepository;
import team14.back.repository.UserRepository;
import team14.back.service.user.UserService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CSRRequestRepository csrRequestRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent())
            return user.get();
        throw new UsernameNotFoundException("User with username: " + username + " not found");
    }

    @Override
    public void register(CSRRequestDTO requestDTO, MultipartFile document) throws IOException {
        CSRRequest csrRequest = new CSRRequest();
        csrRequest.setEmail(requestDTO.getEmail());
        csrRequest.setFirstName(requestDTO.getFirstName());
        csrRequest.setLastName(requestDTO.getLastName());
        csrRequest.setTimestamp(LocalDateTime.now());

        String filePath = "src/main/resources/data/csr/"+requestDTO.getEmail()+".csr";
        File path = new File(filePath);
        path.createNewFile();
        FileOutputStream output = new FileOutputStream(path);
        output.write(document.getBytes());
        output.close();

        csrRequestRepository.save(csrRequest);
    }

    @Override
    public LoginDTO createNewUser(String email) {
        if (this.userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("Can't create user with email: " + email + " because user already exist");
        }
        CSRRequest csrRequest = csrRequestRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Can't find csr request for user with email: " + email));

        String password = generatePassword();
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(
                email,
                csrRequest.getFirstName(),
                csrRequest.getLastName(),
                encodedPassword,
                false,
                new Role(2L, "ROLE_OWNER"));

        this.userRepository.save(user);
        return new LoginDTO(email, password);
    }

    @Override
    public void blockUser(String email) {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Can't find user with email: " + email));
        user.setBlocked(true);
        this.userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }


    public static String generatePassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[14];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
