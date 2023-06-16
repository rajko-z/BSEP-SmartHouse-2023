package team14.back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team14.back.model.DeviceMessage;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReportDataDTO {
    private List<DeviceMessageDTO> deviceMessages;

    @PositiveOrZero
    private int sumInfoMessages;
    @PositiveOrZero
    private int sumWarningMessages;
    @PositiveOrZero
    private int sumErrorMessages;
}
