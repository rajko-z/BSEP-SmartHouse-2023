package team14.back.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team14.back.model.CSRRequest;

public interface CSRRequestRepository extends MongoRepository<CSRRequest, Long> {

}
