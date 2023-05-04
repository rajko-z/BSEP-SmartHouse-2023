package team14.back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import team14.back.model.CSRRequest;
import team14.back.model.Role;
import team14.back.model.User;
import team14.back.repository.*;

import java.time.LocalDateTime;

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

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		deleteEverything();
		createRoles();
		createUsers();
		createCSRRequests();
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
	private void createUsers() {
		userRepository.save(new User("smarthouse2023tim14+admin@gmail.com", "Admin", "Admin", "$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG", false, new Role(1L, "ROLE_ADMIN")));
		userRepository.save(new User("smarthouse2023tim14+john@gmail.com", "John", "John", "$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG", false, new Role(2L, "ROLE_OWNER")));
		userRepository.save(new User("smarthouse2023tim14+bob@gmail.com", "Bob", "Bobic", "$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG", false, new Role(3L, "ROLE_TENANT")));
	}

	private void deleteEverything() {
		this.roleRepository.deleteAll();
		this.userRepository.deleteAll();
		this.csrRequestRepository.deleteAll();
		this.loginFailureRepository.deleteAll();
		this.facilityRepository.deleteAll();
	}
}