package team14.back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team14.back.enumerations.DeviceType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeviceInfoDTO {

    private DeviceType deviceType;

    private boolean hasState;

    private List<String> invalidStates;
}
