package team14.back.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import team14.back.model.LoginFailure;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoginFailureRepository extends MongoRepository<LoginFailure, String> {

    @Query("{'email': ?0, 'timestamp': {$gte: ?1}})")
    List<LoginFailure> findNumberOfLoginFailuresInLast10Minutes(String email, LocalDateTime tenMinutesAgo);
}
