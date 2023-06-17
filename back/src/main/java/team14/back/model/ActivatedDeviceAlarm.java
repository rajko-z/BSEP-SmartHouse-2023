package team14.back.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("activated_device_alarms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ActivatedDeviceAlarm {

    private Date timestamp;
    private DeviceAlarmTrigger deviceAlarmTrigger;
    private String message;
    private String facilityName;
}
