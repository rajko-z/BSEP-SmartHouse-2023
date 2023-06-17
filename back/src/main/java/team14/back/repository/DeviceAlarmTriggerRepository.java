package team14.back.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team14.back.model.DeviceAlarmTrigger;

import java.util.Optional;

public interface DeviceAlarmTriggerRepository extends MongoRepository<DeviceAlarmTrigger, String> {
    Optional<DeviceAlarmTrigger> findByAlarmName(String alarmName);
}
