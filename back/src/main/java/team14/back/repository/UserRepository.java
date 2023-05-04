package team14.back.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team14.back.model.Role;
import team14.back.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
}
