package team14.back.converters;

import team14.back.dto.alarms.ActivatedDeviceAlarmDTO;
import team14.back.dto.alarms.trigger.DeviceAlarmTriggerDTO;
import team14.back.model.ActivatedDeviceAlarm;
import team14.back.model.DeviceAlarmTrigger;

import java.time.ZoneId;

public class AlarmConverters {

    private AlarmConverters() {}

    public static DeviceAlarmTriggerDTO convertTrigger(DeviceAlarmTrigger trigger) {
        return DeviceAlarmTriggerDTO.builder()
                .alarmName(trigger.getAlarmName())
                .higherThan(trigger.getHigherThan())
                .lowerThan(trigger.getLowerThan())
                .invalidState(trigger.getInvalidState())
                .ruleName(trigger.getRuleName())
                .deviceType(trigger.getDeviceType())
                .build();
    }

    public static ActivatedDeviceAlarmDTO convertActivatedDeviceAlarm(ActivatedDeviceAlarm activatedDeviceAlarm) {
        return ActivatedDeviceAlarmDTO.builder()
                .deviceAlarmTrigger(convertTrigger(activatedDeviceAlarm.getDeviceAlarmTrigger()))
                .facilityName(activatedDeviceAlarm.getFacilityName())
                .timestamp(activatedDeviceAlarm.getTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .message(activatedDeviceAlarm.getMessage())
                .build();
    }
}
