package team14.back.service.device;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team14.back.dto.UpdateDeviceStateDTO;
import team14.back.model.DeviceMessage;
import team14.back.repository.DeviceRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DeviceServiceImpl implements DeviceService{

    private final DeviceRepository deviceRepository;

    @Override
    public void updateDeviceState(UpdateDeviceStateDTO newDeviceState) {
        String filename = newDeviceState.getId().toString().concat("messages.json");
        List<DeviceMessage> deviceMessages = deviceRepository.getDeviceMessages(newDeviceState.getId().toString());
        deviceMessages.add(new DeviceMessage(newDeviceState));
        deviceRepository.saveDeviceMessages(filename, deviceMessages);
    }
}
