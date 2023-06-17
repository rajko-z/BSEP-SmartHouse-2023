package team14.back.dto.alarms.trigger;

import lombok.*;
import team14.back.enumerations.DeviceType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DeviceAlarmTriggerDTO {
    private String alarmName;
    private DeviceType deviceType;
    private double lowerThan;
    private double higherThan;
    private String invalidState;
    private String ruleName;
}
