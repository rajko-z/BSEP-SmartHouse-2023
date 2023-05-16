package team14.back.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateDeviceStateDTO {
    private Long id;
    private String state;
    private String message;
    private String messageType;

}
