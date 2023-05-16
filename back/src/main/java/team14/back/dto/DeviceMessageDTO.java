package team14.back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import team14.back.model.DeviceMessage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeviceMessageDTO {
    @NotBlank
    @Length(max=256)
    private String message;

    @NotBlank
    @Length(max=256)
    private String messageType;

    @NotBlank
    private LocalDateTime timestamp;

    @NotBlank
    @Length(max=256)
    private String deviceStatus;

    public DeviceMessageDTO(DeviceMessage deviceMessage)
    {
        this.message = deviceMessage.getMessage();
        this.messageType = String.valueOf(deviceMessage.getMessageType());
        this.timestamp = deviceMessage.getTimestamp();
        this.deviceStatus = deviceMessage.getDeviceStatus();
    }
}
