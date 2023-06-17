package team14.back.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team14.back.model.ActivatedDeviceAlarm;
import java.util.List;

public interface ActivatedDeviceAlarmRepository extends MongoRepository<ActivatedDeviceAlarm, String> {

    List<ActivatedDeviceAlarm> findByFacilityName(String facilityName);
}
