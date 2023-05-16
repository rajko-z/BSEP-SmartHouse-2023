package team14.back.service.device;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team14.back.dto.UpdateDeviceStateDTO;
import team14.back.model.DeviceMessage;
import team14.back.repository.DeviceRepository;

import java.util.List;
import team14.back.dto.DeviceMessageDTO;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeviceServiceImpl implements DeviceService{

    private final DeviceRepository deviceRepository;

    @Override
    public DeviceMessage updateDeviceState(UpdateDeviceStateDTO newDeviceState) {
        String filename = newDeviceState.getId().toString().concat("messages.json");
        List<DeviceMessage> deviceMessages = deviceRepository.getDeviceMessages(newDeviceState.getId().toString());
        DeviceMessage deviceMessage = new DeviceMessage(newDeviceState);
        deviceMessages.add(deviceMessage);
        deviceRepository.saveDeviceMessages(filename, deviceMessages);
        return deviceMessage;
    }

    public List<DeviceMessageDTO> getDeviceMessages(List<String> deviceMessagesPaths) {
        return deviceMessagesPaths.stream()
                .flatMap(path -> this.deviceRepository.getDeviceMessages(path).stream())
                .map(DeviceMessageDTO::new)
                .collect(Collectors.toList());
    }
}
