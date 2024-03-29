package team14.back.service.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team14.back.dto.*;
import team14.back.dto.csr.CSRRequestDTO;
import team14.back.dto.login.LoginDTO;
import team14.back.enumerations.FacilityType;
import team14.back.enumerations.LogAction;
import team14.back.exception.BadRequestException;
import team14.back.exception.NotFoundException;
import team14.back.model.CSRRequest;
import team14.back.model.Facility;
import team14.back.model.Role;
import team14.back.model.User;
import team14.back.repository.CSRRequestRepository;
import team14.back.repository.FacilityRepository;
import team14.back.repository.RoleRepository;
import team14.back.repository.UserRepository;
import team14.back.service.log.LogService;
import team14.back.utils.CommonPasswords;
import team14.back.utils.ExceptionMessageConstants;
import team14.back.utils.HttpUtils;

import javax.servlet.http.HttpServletRequest;
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

    private static final String CLS_NAME = UserServiceImpl.class.getName();
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CSRRequestRepository csrRequestRepository;
    private final FacilityRepository facilityRepository;

    private final PasswordEncoder passwordEncoder;

    private final LogService logService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent())
            return user.get();
        String errorMessage = "User with username: " + username + " not found";
        throw new UsernameNotFoundException(errorMessage);
    }

    @Override
    public void register(CSRRequestDTO requestDTO, MultipartFile document, HttpServletRequest request) throws IOException {
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

        logService.addInfo(new LogDTO(LogAction.CREATING_NEW_CSR_REQUEST, CLS_NAME, "Creating new csr request for user: " + requestDTO.getEmail(), HttpUtils.getRequestIP(request)));
        csrRequestRepository.save(csrRequest);
    }

    @Override
    public LoginDTO createNewUser(String email, HttpServletRequest request) {
        if (this.userRepository.findByEmail(email).isPresent()) {
            String errorMessage = "Can't create user with email: " + email + " because user already exist";
            logService.addErr(new LogDTO(LogAction.ERROR_ON_CREATING_USER, CLS_NAME, errorMessage, HttpUtils.getRequestIP(request)));
            throw new BadRequestException(errorMessage);
        }
        Optional<CSRRequest> optCsrRequest = csrRequestRepository.findByEmail(email);
        if (optCsrRequest.isEmpty()) {
            String errorMessage = "Can't find csr request for user with email: " + email;
            logService.addErr(new LogDTO(LogAction.ERROR_ON_CREATING_USER, CLS_NAME, errorMessage, HttpUtils.getRequestIP(request)));
            throw new BadRequestException(errorMessage);
        }
        CSRRequest csrRequest = optCsrRequest.get();

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
        logService.addInfo(new LogDTO(LogAction.CREATING_USER, CLS_NAME, "User: " + email + " created.", HttpUtils.getRequestIP(request)));
        return new LoginDTO(email, password);
    }

    @Override
    public void blockUser(String email, HttpServletRequest request) {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Can't find user with email: " + email));
        user.setBlocked(true);
        logService.addInfo(new LogDTO(LogAction.BLOCKING_USER, CLS_NAME, "Blocking user: " + email, HttpUtils.getRequestIP(request)));
        this.userRepository.save(user);
    }

    @Override
    public void unblockUser(String email, HttpServletRequest request) {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Can't find user with email: " + email));
        user.setBlocked(false);
        logService.addInfo(new LogDTO(LogAction.UNBLOCKING_USER, CLS_NAME, "Unblocking user: " + email, HttpUtils.getRequestIP(request)));
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
    public void changePassword(NewPasswordDTO newPasswordDTO, HttpServletRequest request) {
        Optional<User> optUser = userRepository.findByEmail(newPasswordDTO.getEmail());
        if (optUser.isEmpty()) {
            String errorMessage = "Can't find user: " + newPasswordDTO.getEmail();
            logService.addErr(new LogDTO(LogAction.UNKNOWN_USER, CLS_NAME, errorMessage, HttpUtils.getRequestIP(request)));
            throw new UsernameNotFoundException(errorMessage);
        }
        User user = optUser.get();

        if (passwordsMatch(newPasswordDTO.getNewPassword(), user.getPassword())) {
            String errorMessage = newPasswordDTO.getEmail() + " : " +  ExceptionMessageConstants.NEW_PASSWORD_SAME_AS_PREVIOUS;
            logService.addErr(new LogDTO(LogAction.ERROR_ON_CHANGING_PASSWORD, CLS_NAME, errorMessage, HttpUtils.getRequestIP(request)));
            throw new BadRequestException(ExceptionMessageConstants.NEW_PASSWORD_SAME_AS_PREVIOUS);
        }
        if (!passwordsMatch(newPasswordDTO.getCurrentPassword(), user.getPassword())) {
            String errorMessage = newPasswordDTO.getEmail() + " : " +  ExceptionMessageConstants.INVALID_CURRENT_PASSWORD;
            logService.addErr(new LogDTO(LogAction.ERROR_ON_CHANGING_PASSWORD, CLS_NAME, errorMessage, HttpUtils.getRequestIP(request)));
            throw new BadRequestException(ExceptionMessageConstants.INVALID_CURRENT_PASSWORD);
        }
        if (!isNewPasswordInValidFormat(newPasswordDTO.getNewPassword())) {
            String errorMessage = newPasswordDTO.getEmail() + " : " +  ExceptionMessageConstants.FORMAT_FOR_PASSWORD_NOT_VALID;
            logService.addErr(new LogDTO(LogAction.ERROR_ON_CHANGING_PASSWORD, CLS_NAME, errorMessage, HttpUtils.getRequestIP(request)));
            throw new BadRequestException(ExceptionMessageConstants.FORMAT_FOR_PASSWORD_NOT_VALID);
        }
        if (isPasswordOnListOfMostCommonPasswords(newPasswordDTO.getNewPassword())) {
            String errorMessage = newPasswordDTO.getEmail() + " : " +  ExceptionMessageConstants.PASSWORD_ON_LIST_OF_MOST_COMMON_PASSWORDS;
            logService.addErr(new LogDTO(LogAction.ERROR_ON_CHANGING_PASSWORD, CLS_NAME, errorMessage, HttpUtils.getRequestIP(request)));
            throw new BadRequestException(ExceptionMessageConstants.PASSWORD_ON_LIST_OF_MOST_COMMON_PASSWORDS);
        }
        user.setPassword(passwordEncoder.encode(newPasswordDTO.getNewPassword()));
        user.setLastPasswordResetDate(new Date());
        logService.addInfo(new LogDTO(LogAction.CHANGING_PASSWORD, CLS_NAME, "Changing password for user: " + newPasswordDTO.getEmail(), HttpUtils.getRequestIP(request)));
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

    @Override
    @Transactional
    public void saveFacilities(UserFacilitiesDTO userFacilitiesDTO, HttpServletRequest request) {
        User user = this.userRepository.findByEmail(userFacilitiesDTO.getEmail()).
                orElseThrow(() -> new UsernameNotFoundException("Can't find user with email: " + userFacilitiesDTO.getEmail()));

        if(user.getFacilities() != null && user.getFacilities().size() > 0)
        {
            for (FacilityDTO facilityDTO:userFacilitiesDTO.getFacilities()) {
                Facility foundFacility = user.getFacilities().stream().filter(facility -> facility.getName()
                        .equals(facilityDTO.getName())).findFirst().orElse(null);
                if(foundFacility != null)
                    editCurrentFacility(facilityDTO, foundFacility);
                else {
                    Facility facility = new Facility(facilityDTO, user, getUsersFromEmails(facilityDTO));
                    facilityRepository.save(facility);
                    user.getFacilities().add(facility);
                }
            }
            this.removeFacilitiesIfNotFound(user, userFacilitiesDTO.getFacilities());
        }
        else
            createNewFacilities(userFacilitiesDTO, user);

        this.logService.addInfo(new LogDTO(LogAction.SAVING_FACILITIES, CLS_NAME, "Saving facilities for user: " + userFacilitiesDTO.getEmail(), HttpUtils.getRequestIP(request)));
        this.userRepository.save(user);
    }

    private void createNewFacilities(UserFacilitiesDTO userFacilitiesDTO, User user) {
        List<Facility> facilities = new ArrayList<>();
        for (FacilityDTO facilityDTO: userFacilitiesDTO.getFacilities()) {
            Facility facility = new Facility(facilityDTO, user, getUsersFromEmails(facilityDTO));
            facilityRepository.save(facility);
            facilities.add(facility);
        }
        user.setFacilities(facilities);
    }

    private void editCurrentFacility(FacilityDTO facilityDTO, Facility foundFacility) {
        Facility existingFacility = facilityRepository.findByName(foundFacility.getName()).orElseThrow(() -> new NotFoundException("Can't find facility with name: " + foundFacility.getName()));
        existingFacility.setFacilityType(FacilityType.valueOf(facilityDTO.getFacilityType().toUpperCase()));
        existingFacility.setAddress(facilityDTO.getAddress());
        existingFacility.setTenants(getUsersFromEmails(facilityDTO));
        facilityRepository.save(existingFacility);
    }

    private List<User> getUsersFromEmails(FacilityDTO facilityDTO) {
        List<User> users = new ArrayList<>();
        for (String tenantEmail : facilityDTO.getTenantsEmails()) {
            User tenant = this.userRepository.findByEmail(tenantEmail).orElseThrow(() -> new UsernameNotFoundException("Can't find user with email: " + tenantEmail));
            users.add(tenant);
        }
        return users;
    }

    private void removeFacilitiesIfNotFound(User user, List<FacilityDTO> facilities) {
        List<Facility> existingFacilities = user.getFacilities();
        List<Facility> newFacilities = new ArrayList<>();
        for (Facility facility :existingFacilities) {
            if (facilities.stream().filter(fac -> fac.getName().equals(facility.getName())).toList().size() == 0) {
                Facility facility1 = this.facilityRepository.findByName(facility.getName()).orElseThrow(() -> new NotFoundException("Can't find facility with name: " + facility.getName()));
                this.facilityRepository.delete(facility1);
            }
            else{
                newFacilities.add(facility);
            }
        }
        user.setFacilities(newFacilities);
//        existingFacilities.removeIf(existingFacility -> facilities.stream().filter(facility -> facility.getName().equals(existingFacility.getName())).toList().size() > 0);

    }

    @Override
    public List<FacilityDTO> getUserFacilities(String email, HttpServletRequest request) {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Can't find user with email: " + email));
        List<FacilityDTO> facilityDTOS = new ArrayList<>();
        if(user.getFacilities() != null) {
            for (Facility facility : user.getFacilities()) {
                FacilityDTO facilityDTO = new FacilityDTO(facility);
                facilityDTOS.add(facilityDTO);
            }
        }
        logService.addInfo(new LogDTO(LogAction.GET_ALL_FACILITIES, CLS_NAME, "Fetching all facilities for user: " + email, HttpUtils.getRequestIP(request)));
        return facilityDTOS;
    }

    @Override
    public void generateFile(String configFilePath) throws IOException {
        File configFile = new File(configFilePath);

        if (configFile.exists()){
            try {
                deleteFile(configFilePath);
                System.out.println("File content deleted successfully!");
            } catch (IOException e) {
                System.out.println("Failed to delete file content: " + e.getMessage());
            }
        }

        File newConfigFile = new File(configFilePath);
        if (newConfigFile.createNewFile()) {
            System.out.println("File " + configFilePath + " created successfully!");
        } else {
            throw new IOException("File already exists or unable to create the file.");
        }
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        File file = new File(filePath);
        file.delete();
    }


    private void addUserFacilities(AddUserDTO addUserDTO, User user) {
        for(FacilityDTO facilityDTO: addUserDTO.getFacilities())
        {
            String configFilePath = "src/main/resources/data/facilityConfigFiles/" + facilityDTO.getName().replace(" ", "").concat("config.json");
            try {
                generateFile(configFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Facility facility = new Facility(facilityDTO.getName(), facilityTypeConverter(facilityDTO.getFacilityType()),
                    facilityDTO.getAddress(), user, getTenantsByEmail(facilityDTO), configFilePath);
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
    public List<User> getAllUsers(HttpServletRequest request) {
        List<User> allUsers = userRepository.findAll();
        allUsers.forEach(user -> user.setFacilities(null));
        logService.addInfo(new LogDTO(LogAction.GET_ALL_USERS, CLS_NAME, "Fetching all users...", HttpUtils.getRequestIP(request)));
        return allUsers.stream().filter(user -> !user.getRole().getName().equals("ROLE_ADMIN")).collect(Collectors.toList());
    }

    @Override
    public void changeUserRole(ChangeRoleDto changeRoleDto, HttpServletRequest request) {
        User user = userRepository.findByEmail(changeRoleDto.getEmail()).orElseThrow(()-> new NotFoundException("Email "+changeRoleDto.getEmail()+" not found"));

        Role role = roleRepository.findByName(changeRoleDto.getNewRole()).orElseThrow(()-> new NotFoundException("Role "+changeRoleDto.getNewRole()+" not found"));
        user.setRole(role);
        logService.addInfo(new LogDTO(LogAction.CHANGING_USER_ROLE, CLS_NAME, "Changing role for user: " + changeRoleDto.getEmail() + ". New role: " + changeRoleDto.getNewRole(), HttpUtils.getRequestIP(request)));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String userEmail, HttpServletRequest request) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new NotFoundException("Email "+userEmail+" not found"));
        user.setDeleted(true);
        logService.addInfo(new LogDTO(LogAction.DELETING_USER, CLS_NAME, "Deleting user with email: " + userEmail, HttpUtils.getRequestIP(request)));
        userRepository.save(user);
    }

    @Override
    public void undeleteUser(String userEmail, HttpServletRequest request) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new NotFoundException("Email "+userEmail+" not found"));
        user.setDeleted(false);
        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User with "+email+" not found!"));
        user.setFacilities(null);
        return user;
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
