package team14.back.service.device;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team14.back.dto.DeviceMessageDTO;
import team14.back.model.DeviceMessage;
import team14.back.repository.DeviceRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeviceServiceImpl implements DeviceService{

    private final DeviceRepository deviceRepository;

    @Override
    public List<DeviceMessageDTO> getDeviceMessages(List<String> deviceMessagesPaths) {
        return deviceMessagesPaths.stream()
                .flatMap(path -> this.deviceRepository.getDeviceMessages(path).stream())
                .map(DeviceMessageDTO::new)
                .collect(Collectors.toList());
    }
}
