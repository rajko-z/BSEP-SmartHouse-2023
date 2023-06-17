package team14.back.dto.alarms;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AlarmNotificationForUser {
    private ActivatedDeviceAlarmDTO alarm;
    private String userEmail;
}
