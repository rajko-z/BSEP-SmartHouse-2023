package team14.back.service.alarm;

import team14.back.dto.DeviceAlarmTriggerDTO;

import java.util.List;

public interface DeviceAlarmTriggerService {

    List<DeviceAlarmTriggerDTO> getAllDeviceAlarmTriggers();
}
