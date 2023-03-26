package team14.back.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team14.back.model.RemovedCertificate;

public interface RemovedCertificateRepository extends MongoRepository<RemovedCertificate, String> {
}
