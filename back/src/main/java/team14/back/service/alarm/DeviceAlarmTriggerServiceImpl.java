package team14.back.service.alarm;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team14.back.dto.DeviceAlarmTriggerDTO;
import team14.back.repository.DeviceAlarmTriggerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeviceAlarmTriggerServiceImpl implements DeviceAlarmTriggerService{

    private final DeviceAlarmTriggerRepository deviceAlarmTriggerRepository;

    @Override
    public List<DeviceAlarmTriggerDTO> getAllDeviceAlarmTriggers() {
        return deviceAlarmTriggerRepository.findAll().stream()
                .map(t -> new DeviceAlarmTriggerDTO(t.getAlarmName(), t.getDeviceType(), t.getLowerThan(), t.getHigherThan(), t.getInvalidState(), t.getRuleName()))
                .collect(Collectors.toList());
    }
}
