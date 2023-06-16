package team14.back.dto;

import lombok.*;
import team14.back.enumerations.DeviceType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateDeviceStateDTO {
    private Long id;
    private DeviceType deviceType;
    private String state;
    private String message;
    private String messageType;
}
