package team14.back.service.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team14.back.dto.AddUserDTO;
import team14.back.dto.ChangeRoleDto;
import team14.back.dto.FacilityDTO;
import team14.back.dto.NewPasswordDTO;
import team14.back.dto.csr.CSRRequestDTO;
import team14.back.dto.login.LoginDTO;
import team14.back.enumerations.FacilityType;
import team14.back.exception.BadRequestException;
import team14.back.exception.NotFoundException;
import team14.back.model.CSRRequest;
import team14.back.model.Facility;
import team14.back.model.Role;
import team14.back.model.User;
import team14.back.repository.CSRRequestRepository;
import team14.back.repository.RoleRepository;
import team14.back.repository.FacilityRepository;
import team14.back.repository.UserRepository;
import team14.back.utils.CommonPasswords;
import team14.back.utils.ExceptionMessageConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CSRRequestRepository csrRequestRepository;
    private final FacilityRepository facilityRepository;

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
    public void unblockUser(String email) {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Can't find user with email: " + email));
        user.setBlocked(false);
        this.userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public void save(User user) {
        this.userRepository.save(user);
    }

    @Override
    public void changePassword(NewPasswordDTO newPasswordDTO) {
        User user = userRepository.findByEmail(newPasswordDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Can't find username with: " + newPasswordDTO.getEmail()));

        if (passwordsMatch(newPasswordDTO.getNewPassword(), user.getPassword())) {
            throw new BadRequestException(ExceptionMessageConstants.NEW_PASSWORD_SAME_AS_PREVIOUS);
        }
        if (!passwordsMatch(newPasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException(ExceptionMessageConstants.INVALID_CURRENT_PASSWORD);
        }
        if (!isNewPasswordInValidFormat(newPasswordDTO.getNewPassword())) {
            throw new BadRequestException(ExceptionMessageConstants.FORMAT_FOR_PASSWORD_NOT_VALID);
        }
        if (isPasswordOnListOfMostCommonPasswords(newPasswordDTO.getNewPassword())) {
            throw new BadRequestException(ExceptionMessageConstants.PASSWORD_ON_LIST_OF_MOST_COMMON_PASSWORDS);
        }
        user.setPassword(passwordEncoder.encode(newPasswordDTO.getNewPassword()));
        user.setLastPasswordResetDate(new Date());
        userRepository.save(user);
    }

    private boolean isNewPasswordInValidFormat(String password) {
        if (password == null || password.isBlank() || password.length() < 8 || password.length() > 256) {
            return false;
        }
        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^a-zA-Z0-9]).{8,256}$";
        return Pattern.compile(regex).matcher(password).matches();
    }

    private boolean isPasswordOnListOfMostCommonPasswords(String password) {
        return CommonPasswords.getCommonPasswords().stream().anyMatch(c -> c.equals(password));
    }

    private boolean passwordsMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public void addUser(AddUserDTO addUserDTO) {
        if (this.userRepository.findByEmail(addUserDTO.getEmail()).isPresent()) {
            throw new BadRequestException("Can't create user with email: " + addUserDTO.getEmail() + " because user already exist");
        }

        User user = new User();
        user.setDeleted(false);
        user.setRole(new Role(2L, "ROLE_OWNER"));
        user.setEmail(addUserDTO.getEmail());
        user.setFirstName(addUserDTO.getFirstName());
        user.setLastName(addUserDTO.getLastName());
        user.setPassword(passwordEncoder.encode(addUserDTO.getPassword()));
        user.setFacilities(new ArrayList<>());
        addUserFacilities(addUserDTO, user);
        this.userRepository.save(user);
    }

    @Override
    public List<String> getAllNonAdminEmails() {
        List<String> allNonAdminEmails = new ArrayList<>();
        List<User> allUsers = this.userRepository.findAll();
        for(User user: allUsers){
            if (!user.getRole().getName().equals("ROLE_ADMIN"))
                allNonAdminEmails.add(user.getEmail());
        }
        return allNonAdminEmails;
    }

    private void addUserFacilities(AddUserDTO addUserDTO, User user) {
        for(FacilityDTO facilityDTO: addUserDTO.getFacilities())
        {
            Facility facility = new Facility(facilityDTO.getName(), facilityTypeConverter(facilityDTO.getFacilityType()),
                    facilityDTO.getAddress(), user, getTenantsByEmail(facilityDTO));
            this.facilityRepository.save(facility);
            user.getFacilities().add(facility);
        }
    }

    private List<User> getTenantsByEmail(FacilityDTO facilityDTO){
        List<User> tenants = new ArrayList<>();
        for(String tenantEmail: facilityDTO.getTenantsEmails()){
            User foundTenant = this.userRepository.findByEmail(tenantEmail).orElseThrow(() ->
                    new BadRequestException("Can't find csr request for user with email: " + tenantEmail));
            tenants.add(foundTenant);
        }
        return tenants;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream().filter(user -> !user.getRole().getName().equals("ROLE_ADMIN")).collect(Collectors.toList());
    }

    @Override
    public void changeUserRole(ChangeRoleDto changeRoleDto) {
        User user = userRepository.findByEmail(changeRoleDto.getEmail()).orElseThrow(()-> new NotFoundException("Email "+changeRoleDto.getEmail()+" not found"));

        Role role = roleRepository.findByName(changeRoleDto.getNewRole()).orElseThrow(()-> new NotFoundException("Role "+changeRoleDto.getNewRole()+" not found"));
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new NotFoundException("Email "+userEmail+" not found"));
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public void undeleteUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new NotFoundException("Email "+userEmail+" not found"));
        user.setDeleted(false);
        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User with "+email+" not found!"));
    }

    private FacilityType facilityTypeConverter(String rawFacilityType)
    {
        if(rawFacilityType.equals("House"))
            return FacilityType.HOUSE;
        else if(rawFacilityType.equals("Apartment"))
            return FacilityType.APARTMENT;
        return FacilityType.COTTAGE;
    }

    public static String generatePassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[14];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
