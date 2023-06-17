package team14.back.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import team14.back.enumerations.DeviceType;

@Document("device_alarm_triggers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeviceAlarmTrigger {

    private String alarmName;

    private DeviceType deviceType;

    private double lowerThan;

    private double higherThan;

    private String invalidState;

    private String ruleName;
}
