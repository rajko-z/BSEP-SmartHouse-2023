package team14.back.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import team14.back.dto.UpdateDeviceStateDTO;
import team14.back.enumerations.MessageType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DeviceMessage {
    private String message;
    private MessageType messageType;
    @JsonFormat(pattern = "dd.MM.yyyy. HH:mm:ss")
    private LocalDateTime timestamp;
    private String deviceStatus;

    public DeviceMessage(UpdateDeviceStateDTO newDeviceState){
        this.message = newDeviceState.getMessage();
        this.messageType = MessageType.valueOf(newDeviceState.getMessageType());
        this.timestamp = LocalDateTime.now();
        this.deviceStatus = newDeviceState.getState();
    }
}
