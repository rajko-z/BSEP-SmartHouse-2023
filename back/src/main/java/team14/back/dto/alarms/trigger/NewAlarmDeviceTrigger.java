package team14.back.dto.alarms.trigger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team14.back.enumerations.DeviceType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewAlarmDeviceTrigger {
    @NotBlank
    private String alarmName;

    @NotNull
    private DeviceType selectedDevice;

    @NotNull
    private boolean hasState;

    @NotNull
    private String state;

    @NotNull
    private boolean greater;

    @Min(0)
    @Max(1000)
    private double value;
}
