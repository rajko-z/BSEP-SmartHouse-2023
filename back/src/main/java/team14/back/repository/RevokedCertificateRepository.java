package team14.back.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team14.back.model.RevokedCertificate;

public interface RevokedCertificateRepository extends MongoRepository<RevokedCertificate, String> {
}
