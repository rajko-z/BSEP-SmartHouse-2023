package team14.back.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team14.back.model.Role;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, Long> {

    Optional<Role> findByName(String newRole);
}
