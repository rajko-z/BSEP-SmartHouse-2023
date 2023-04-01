package team14.back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import team14.back.model.CSRRequest;
import team14.back.model.RemovedCertificate;
import team14.back.model.Role;
import team14.back.model.User;
import team14.back.repository.CSRRequestRepository;
import team14.back.repository.RemovedCertificateRepository;
import team14.back.repository.RoleRepository;
import team14.back.repository.UserRepository;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableMongoRepositories
public class BackApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RemovedCertificateRepository removedCertificateRepository;

	@Autowired
	private CSRRequestRepository csrRequestRepository;

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		createRoles();
		createUsers();
		createCSRRequests();
		createRemoveCertificates();
	}

	private void createRoles() {
		roleRepository.save(new Role(1L, "ROLE_ADMIN"));
		roleRepository.save(new Role(2L, "ROLE_OWNER"));
		roleRepository.save(new Role(3L, "ROLE_TENANT"));
	}

	private void createRemoveCertificates() {
		removedCertificateRepository.save(new RemovedCertificate("alias1", "neki razlog1"));
		removedCertificateRepository.save(new RemovedCertificate("alias2", "neki razlog2"));
	}

	private void createCSRRequests() {
		csrRequestRepository.save(new CSRRequest(11111111L, "test1@gmail.com", "test1", "test1", "test1fajl", LocalDateTime.now().minusDays(5)));
		csrRequestRepository.save(new CSRRequest(222232222L, "test2gmail.com", "test2", "test2", "test2fajl", LocalDateTime.now().minusDays(2)));
		csrRequestRepository.save(new CSRRequest(222212222L, "test3gmail.com", "test3", "test3", "test3fajl", LocalDateTime.now().minusDays(10).minusHours(2)));
		csrRequestRepository.save(new CSRRequest(432433332L, "test4gmail.com", "test4", "test4", "test4fajl", LocalDateTime.now().minusDays(4).minusMinutes(3)));
		csrRequestRepository.save(new CSRRequest(445555555L, "test5gmail.com", "test5", "test5", "test5fajl", LocalDateTime.now().minusDays(4).minusMinutes(3)));
	}

	// sifra za sve korisnike je 12345678
	private void createUsers() {
		userRepository.save(new User("test1@gmail.com", "Test1", "Test1", "$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG", false, new Role(1L, "ROLE_ADMIN")));
		userRepository.save(new User("test2@gmail.com", "Test2", "Test2", "$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG", false, new Role(2L, "ROLE_OWNER")));
		userRepository.save(new User("test3@gmail.com", "Test3", "Test3", "$2a$10$GWugnfZGCvK0X3W4NYXE5OYyfNvSaEvhlpK8zrdF0WVd3nvtLZfuG", false, new Role(3L, "ROLE_TENANT")));
	}
}