package team14.back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team14.back.enumerations.LogAction;
import team14.back.enumerations.LogStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AlarmDTO {
    private LogStatus logStatus;
    private LogAction logActionType;
    private String message;
}
