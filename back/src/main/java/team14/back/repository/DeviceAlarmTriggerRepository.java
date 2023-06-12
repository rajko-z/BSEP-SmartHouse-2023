package team14.back.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team14.back.model.DeviceAlarmTrigger;

public interface DeviceAlarmTriggerRepository extends MongoRepository<DeviceAlarmTrigger, String> {

}
