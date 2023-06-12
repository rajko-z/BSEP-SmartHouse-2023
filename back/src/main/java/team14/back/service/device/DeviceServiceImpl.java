package team14.back.service.device;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import team14.back.dto.DeviceInfoDTO;
import team14.back.dto.UpdateDeviceStateDTO;
import team14.back.enumerations.DeviceType;
import team14.back.model.DeviceMessage;
import team14.back.repository.DeviceRepository;

import java.util.Comparator;
import java.util.List;
import team14.back.dto.DeviceMessageDTO;
import team14.back.service.facility.FacilityService;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeviceServiceImpl implements DeviceService{

    private final DeviceRepository deviceRepository;
    private final FacilityService facilityService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void updateDeviceState(UpdateDeviceStateDTO newDeviceState) {
        String filename = newDeviceState.getId().toString().concat("messages.json");
        List<DeviceMessage> deviceMessages = deviceRepository.getDeviceMessages(filename);
        DeviceMessage deviceMessage = new DeviceMessage(newDeviceState);
        deviceMessages.add(deviceMessage);
        deviceRepository.saveDeviceMessages(filename, deviceMessages);
        DeviceMessageDTO deviceMessageDTO = new DeviceMessageDTO(deviceMessage);
        String facilityName = this.facilityService.getFacilityNameByDeviceId(newDeviceState.getId());
        this.simpMessagingTemplate.convertAndSend("/device-messages/new-message/"+facilityName, deviceMessageDTO);

    }

    public List<DeviceMessageDTO> getDeviceMessages(List<String> deviceMessagesPaths) {
        return deviceMessagesPaths.stream()
                .flatMap(path -> this.deviceRepository.getDeviceMessages(path).stream())
                .map(DeviceMessageDTO::new)
                .sorted(Comparator.comparing(DeviceMessageDTO::getTimestamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceInfoDTO> getAllDeviceInfos() {
        return deviceRepository.getDevicesInfo();
    }

    @Override
    public DeviceInfoDTO getDeviceInfoByType(DeviceType type) {
        List<DeviceInfoDTO> devices = deviceRepository.getDevicesInfo();
        for (DeviceInfoDTO device : devices) {
            if (device.getDeviceType().equals(type)) {
                return device;
            }
        }
        return null;
    }
}
