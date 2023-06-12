package team14.back.service.alarm;

import team14.back.dto.DeviceAlarmTriggerDTO;
import team14.back.dto.NewAlarmDeviceTrigger;

import java.util.List;

public interface DeviceAlarmTriggerService {

    List<DeviceAlarmTriggerDTO> getAllDeviceAlarmTriggers();

    void addNewTrigger(NewAlarmDeviceTrigger trigger);
}
