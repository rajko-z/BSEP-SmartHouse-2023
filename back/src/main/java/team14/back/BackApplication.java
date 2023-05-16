package team14.back;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import team14.back.enumerations.FacilityType;
import team14.back.model.*;
import team14.back.repository.*;

import java.io.File;
import java.io.IOException;
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
	}
}