package team14.back.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team14.back.model.Facility;
import team14.back.model.User;

public interface FacilityRepository extends MongoRepository<Facility, String> {
}
