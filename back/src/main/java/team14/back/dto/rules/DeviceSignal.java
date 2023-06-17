package team14.back.dto.rules;

import lombok.*;
import team14.back.enumerations.DeviceType;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DeviceSignal {
    private Long deviceId;
    private DeviceType deviceType;
    private String state;
    private String message;
    private double temperature;
    private List<String> activatedAlarms;
}
