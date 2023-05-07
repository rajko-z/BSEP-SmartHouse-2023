package team14.back.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team14.back.model.Facility;
import team14.back.model.User;

import java.util.Optional;

public interface FacilityRepository extends MongoRepository<Facility, String> {
    Optional<Facility> findByName(String name);
}
