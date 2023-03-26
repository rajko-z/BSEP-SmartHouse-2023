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
import team14.back.repository.UserRepository;

@SpringBootApplication
@EnableMongoRepositories
public class BackApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RemovedCertificateRepository removedCertificateRepository;

	@Autowired
	private CSRRequestRepository csrRequestRepository;

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		createUsers();
		createCSRRequests();
		createRemoveCertificates();
	}

	private void createRemoveCertificates() {
		removedCertificateRepository.save(new RemovedCertificate("alias1", "neki razlog1"));
		removedCertificateRepository.save(new RemovedCertificate("alias2", "neki razlog2"));
	}

	private void createCSRRequests() {
		csrRequestRepository.save(new CSRRequest(11111111L, "test1@gmail.com", "test1", "test1", "test1fajl"));
		csrRequestRepository.save(new CSRRequest(22222222L, "test2gmail.com", "test2", "test2", "test2fajl"));
	}

	private void createUsers() {
		userRepository.save(new User("test1@gmail.com", "Test1", "Test1", "", false, new Role(1L, "ROLE_ADMIN")));
		userRepository.save(new User("test2@gmail.com", "Test2", "Test2", "", false, new Role(2L, "ROLE_OWNER")));
		userRepository.save(new User("test3@gmail.com", "Test3", "Test3", "", false, new Role(3L, "ROLE_NESTO")));
	}
}