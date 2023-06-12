package team14.back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import team14.back.enumerations.DeviceType;
import team14.back.enumerations.FacilityType;
import team14.back.enumerations.LogAction;
import team14.back.enumerations.LogStatus;
import team14.back.model.*;
import team14.back.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableMongoRepositories
@EnableAsync
public class BackApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RevokedCertificateRepository revokedCertificateRepository;

	@Autowired
	private CSRRequestRepository csrRequestRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private LoginFailureRepository loginFailureRepository;

	@Autowired
	private FacilityRepository facilityRepository;

	@Autowired
	private LogRepository logRepository;

	@Autowired
	private DeviceAlarmTriggerRepository deviceAlarmTriggerRepository;

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		deleteEverything();
		List<Facility> initialFacilities = createFacilities();
		createRoles();
		createUsers(initialFacilities);
		createCSRRequests();
		createLogs();
		createDeviceAlarmTriggers();
	}

	private void createLogs() {
		logRepository.save(new Log(LogStatus.INFO, LogAction.LOG_IN_SUCCESS, LocalDateTime.now().minusDays(1).minusHours(2), "AuthController", "user smarthouse2023tim14+john@gmail.com successfully logged in"));
		logRepository.save(new Log(LogStatus.ERROR, LogAction.INVALID_CREDENTIALS, LocalDateTime.now().minusDays(2).minusHours(1), "AuthController", "invalid credentials for smarthouse2023tim14+john@gmail.com"));
		logRepository.save(new Log(LogStatus.INFO, LogAction.INVALID_2FA, LocalDateTime.now().minusDays(2), "AuthController", "invalid 2FA for smarthouse2023tim14+john@gmail.com"));
		logRepository.save(new Log(LogStatus.INFO, LogAction.GET_ALL_USERS, LocalDateTime.now().minusDays(3), "UsersService", "Fetching all users"));
		logRepository.save(new Log(LogStatus.INFO, LogAction.GET_ALL_CSR_REQUESTS, LocalDateTime.now().minusDays(4), "CSRRequestsService", "Fetching all csr requests"));
		logRepository.save(new Log(LogStatus.INFO, LogAction.REVOKE_CERTIFICATE, LocalDateTime.now().minusDays(2), "CertificateService", "Revoking certificate"));
	}

	private void createDeviceAlarmTriggers() {
		deviceAlarmTriggerRepository.save(new DeviceAlarmTrigger("HIGH_TEMP_THERMOMETER", DeviceType.THERMOMETER, 0.0, 30.0, "", "Thermometar temperature higher than 30"));
		deviceAlarmTriggerRepository.save(new DeviceAlarmTrigger("GATE_BLOCKED", DeviceType.GATE, 0.0, 0.0, "BLOCKED", "Door blocked"));
		deviceAlarmTriggerRepository.save(new DeviceAlarmTrigger("AIR_CONDITIONING_HIGH_VOLTAGE", DeviceType.AIR_CONDITIONING, 0.0, 0.0, "HIGH VOLTAGE", "Air conditioning is at high voltage"));
	}

	private List<Facility> createFacilities() {
		List<Facility> facilities = new ArrayList<>();

		Facility facility = new Facility("kuca", FacilityType.HOUSE, "Rumenacki put 125", new User(),
				new ArrayList<>(), "src/main/resources/data/facilityConfigFiles/kucaconfig.json");

		facilityRepository.save(facility);
		facilities.add(facility);

		return facilities;
	}

	private void createRoles() {
		roleRepository.save(new Role(1L, "ROLE_ADMIN"));
		roleRepository.save(new Role(2L, "ROLE_OWNER"));
		roleRepository.save(new Role(3L, "ROLE_TENANT"));
	}

	private void createCSRRequests() {
		csrRequestRepository.save(new CSRRequest("rajkozgrc4@gmail.com", "test1", "test1", LocalDateTime.now().minusDays(5)));
		csrRequestRepository.save(new CSRRequest("test2gmail.com", "test2", "test2", LocalDateTime.now().minusDays(2)));
		csrRequestRepository.save(new CSRRequest("test3gmail.com", "test3", "test3", LocalDateTime.now().minusDays(10).minusHours(2)));
		csrRequestRepository.save(new CSRRequest("test4gmail.com", "test4", "test4", LocalDateTime.now().minusDays(4).minusMinutes(3)));
		csrRequestRepository.save(new CSRRequest("test5gmail.com", "test5", "test5", LocalDateTime.now().minusDays(4).minusMinutes(3)));
	}

	// sifra za sve korisnike je 12345678
	private void createUsers(List<Facility> initialFacilities) {
		userRepository.save(new User("smarthouse2023tim14+admin@gmail.com", "Admin", "Admin", "$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG", false, new Role(1L, "ROLE_ADMIN")));
		User owner = new User("smarthouse2023tim14+john@gmail.com", "John", "John", "$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG", false, new Role(2L, "ROLE_OWNER"));
		owner.setFacilities(initialFacilities);
		for(Facility facility: initialFacilities){
			facility.setOwner(owner);
			facilityRepository.save(facility);
		}
		userRepository.save(owner);
		userRepository.save(new User("smarthouse2023tim14+bob@gmail.com", "Bob", "Bobic", "$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG", false, new Role(3L, "ROLE_TENANT")));
	}

	private void deleteEverything() {
		this.roleRepository.deleteAll();
		this.userRepository.deleteAll();
		this.csrRequestRepository.deleteAll();
		this.loginFailureRepository.deleteAll();
		this.facilityRepository.deleteAll();
		this.revokedCertificateRepository.deleteAll();
		this.logRepository.deleteAll();
		this.deviceAlarmTriggerRepository.deleteAll();
	}
}