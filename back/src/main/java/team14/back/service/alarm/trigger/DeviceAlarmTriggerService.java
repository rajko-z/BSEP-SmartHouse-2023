package team14.back.service.alarm.trigger;

import team14.back.dto.alarms.trigger.DeviceAlarmTriggerDTO;
import team14.back.dto.alarms.trigger.NewAlarmDeviceTrigger;
import team14.back.model.DeviceAlarmTrigger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface DeviceAlarmTriggerService {

    List<DeviceAlarmTriggerDTO> getAllDeviceAlarmTriggers();

    void addNewTrigger(NewAlarmDeviceTrigger trigger, HttpServletRequest request);

    DeviceAlarmTrigger findTriggerByName(String name, HttpServletRequest request);
}
