package team14.back.service.device;

import team14.back.dto.DeviceInfoDTO;
import team14.back.dto.UpdateDeviceStateDTO;
import team14.back.dto.DeviceMessageDTO;
import team14.back.enumerations.DeviceType;
import team14.back.model.DeviceMessage;

import java.util.List;

public interface DeviceService {
    List<DeviceMessageDTO> getDeviceMessages(List<String> deviceMessagesPaths);

    void updateDeviceState(UpdateDeviceStateDTO newDeviceState);

    List<DeviceInfoDTO> getAllDeviceInfos();

    DeviceInfoDTO getDeviceInfoByType(DeviceType type);
}
