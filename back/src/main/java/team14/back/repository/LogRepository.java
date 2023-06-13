package team14.back.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team14.back.model.Log;

public interface LogRepository extends MongoRepository<Log, String> {
}
