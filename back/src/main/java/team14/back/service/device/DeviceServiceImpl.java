package team14.back.service.device;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import team14.back.dto.UpdateDeviceStateDTO;
import team14.back.exception.BadRequestException;
import team14.back.model.Device;
import team14.back.model.DeviceMessage;
import team14.back.model.Facility;
import team14.back.repository.DeviceRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import team14.back.dto.DeviceMessageDTO;
import team14.back.repository.FacilityRepository;
import team14.back.service.facility.FacilityService;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeviceServiceImpl implements DeviceService{

    private final DeviceRepository deviceRepository;
    private final FacilityService facilityService;
    private final FacilityRepository facilityRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void updateDeviceState(UpdateDeviceStateDTO newDeviceState) {
        String filename = newDeviceState.getId().toString().concat("messages.json");
        List<DeviceMessage> deviceMessages = deviceRepository.getDeviceMessages(filename);
        DeviceMessage deviceMessage = new DeviceMessage(newDeviceState);
        deviceMessages.add(deviceMessage);
        deviceRepository.saveDeviceMessages(filename, deviceMessages);
        sendIfValidRegex(newDeviceState, deviceMessage);

    }

    @Override
    public Device findDeviceById(long id) {
        for (Facility facility : this.facilityRepository.findAll()) {
            List<Device> devices = this.deviceRepository.getDevicesFromFacility(facility.getName());
            Optional<Device> device = devices.stream().filter(device1 -> device1.getId() == id).findFirst();
            if(device.isPresent())
                return device.get();
        }
        return null;
    }

    @Override
    public boolean isValidRegex(String pattern) {
        try {
            Pattern.compile(pattern);
            return true;
        } catch (PatternSyntaxException exception) {
            return false;
        }
    }

    private void sendIfValidRegex(UpdateDeviceStateDTO newDeviceState, DeviceMessage deviceMessage) {
        Device device = findDeviceById(newDeviceState.getId());
        if(!isValidRegex(device.getRegexFilter()))
            throw new BadRequestException("Regex: "+device.getRegexFilter()+" for device "+device.getId()+", "+device.getDeviceType()+" is not valid");

        String regexFilter = ".*"+device.getRegexFilter()+".*";
        if(deviceMessage.getMessage().matches(regexFilter)){
            DeviceMessageDTO deviceMessageDTO = new DeviceMessageDTO(deviceMessage);
            String facilityName = this.facilityService.getFacilityNameByDeviceId(newDeviceState.getId());
            this.simpMessagingTemplate.convertAndSend("/device-messages/new-message/"+facilityName, deviceMessageDTO);
        }
    }

    public List<DeviceMessageDTO> getDeviceMessages(List<String> deviceMessagesPaths) {
        List<DeviceMessageDTO> deviceMessageDTOS = new ArrayList<>();
        for (String deviceMessagePath : deviceMessagesPaths) {
            long deviceId = Long.parseLong(deviceMessagePath.replace("messages.json", ""));
            Device device = this.findDeviceById(deviceId);

            String regexFilter = ".*"+device.getRegexFilter()+".*";
            if(!isValidRegex(device.getRegexFilter()))
                throw new BadRequestException("Regex: "+device.getRegexFilter()+" for device "+device.getId()+", "+device.getDeviceType()+" is not valid");

            List<DeviceMessage> filteredDeviceMessages = this.deviceRepository.getDeviceMessages(deviceMessagePath)
                    .stream().filter(deviceMessage -> deviceMessage.getMessage().matches(regexFilter))
                    .sorted(Comparator.comparing(DeviceMessage::getTimestamp)).toList();


//            List<DeviceMessage> filteredDeviceMessages = new ArrayList<>();
//            for (DeviceMessage devMessage :this.deviceRepository.getDeviceMessages(deviceMessagePath)) {
//                String mess = devMessage.getMessage();
//                if (mess.matches(regexFilter))
//                    filteredDeviceMessages.add(devMessage);
//            }

//            filteredDeviceMessages = filteredDeviceMessages.stream().sorted(Comparator.comparing(DeviceMessage::getTimestamp)).toList();
            deviceMessageDTOS.addAll(filteredDeviceMessages.stream().map(DeviceMessageDTO::new).toList());

        }

        return deviceMessageDTOS;
    }
}
