package team14.back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team14.back.enumerations.DeviceType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeviceAlarmTriggerDTO {
    private String alarmName;
    private DeviceType deviceType;
    private double lowerThan;
    private double higherThan;
    private String invalidState;
    private String ruleName;
}
