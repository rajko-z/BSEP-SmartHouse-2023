package team14.back.service.device;

import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import team14.back.dto.ReportDataDTO;
import team14.back.dto.UpdateDeviceStateDTO;
import team14.back.enumerations.MessageType;
import team14.back.model.DeviceMessage;
import team14.back.repository.DeviceRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import team14.back.dto.DeviceMessageDTO;
import team14.back.service.facility.FacilityService;

import java.util.Locale;
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

    @Override
    public ReportDataDTO getReportData(String startDate, String endDate, List<String> deviceMessagesPaths) {
        List<DeviceMessageDTO> allDeviceMessageDTOS = getDeviceMessages(deviceMessagesPaths);
        List<DeviceMessageDTO> wantedDeviceMessageDTOS = new ArrayList<>();

        for (DeviceMessageDTO deviceMessageDTO : allDeviceMessageDTOS) {
            if (deviceMessageDTO.getTimestamp().isAfter(customDateParse(startDate)) &&
                deviceMessageDTO.getTimestamp().isBefore(customDateParse(endDate))) {
                wantedDeviceMessageDTOS.add(deviceMessageDTO);
            }
        }

        return new ReportDataDTO(
            wantedDeviceMessageDTOS,
            (int) wantedDeviceMessageDTOS.stream().filter(message -> message.getMessageType().equals(String.valueOf(MessageType.INFO))).count(),
            (int) wantedDeviceMessageDTOS.stream().filter(message -> message.getMessageType().equals(String.valueOf(MessageType.WARNING))).count(),
            (int) wantedDeviceMessageDTOS.stream().filter(message -> message.getMessageType().equals(String.valueOf(MessageType.ERROR))).count()
        );
    }

    @Override
    public LocalDateTime customDateParse(String rawDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
        String[] words = rawDate.split(" ");
        String day = words[2];
        String month = monthTransformer(words[1]);
        String year = words[3];
        String time = words[4];
        String date = day + "." + month + "." + year + ". " + time;
        return LocalDateTime.parse(date, formatter);
    }

    @Override
    public String monthTransformer(String rawMonth) {
        return switch (rawMonth) {
            case "Jan" -> "01";
            case "Feb" -> "02";
            case "Mar" -> "03";
            case "Apr" -> "04";
            case "May" -> "05";
            case "Jun" -> "06";
            case "Jul" -> "07";
            case "Aug" -> "08";
            case "Sep" -> "09";
            case "Oct" -> "10";
            case "Nov" -> "11";
            case "Dec" -> "12";
            default -> "Unknown";
        };
    }

    public List<DeviceMessageDTO> getDeviceMessages(List<String> deviceMessagesPaths) {
        return deviceMessagesPaths.stream()
                .flatMap(path -> this.deviceRepository.getDeviceMessages(path).stream())
                .map(DeviceMessageDTO::new)
                .sorted(Comparator.comparing(DeviceMessageDTO::getTimestamp))
                .collect(Collectors.toList());
    }
}
