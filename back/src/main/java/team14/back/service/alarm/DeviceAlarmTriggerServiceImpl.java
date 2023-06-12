package team14.back.service.alarm;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team14.back.dto.DeviceAlarmTriggerDTO;
import team14.back.dto.DeviceInfoDTO;
import team14.back.dto.LogDTO;
import team14.back.dto.NewAlarmDeviceTrigger;
import team14.back.enumerations.LogAction;
import team14.back.exception.BadRequestException;
import team14.back.model.DeviceAlarmTrigger;
import team14.back.repository.DeviceAlarmTriggerRepository;
import team14.back.service.device.DeviceService;
import team14.back.service.log.LogService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeviceAlarmTriggerServiceImpl implements DeviceAlarmTriggerService{

    private static final String CLS_NAME = DeviceAlarmTriggerServiceImpl.class.getName();
    private final DeviceAlarmTriggerRepository deviceAlarmTriggerRepository;

    private final DeviceService deviceService;

    private final LogService logService;

    @Override
    public List<DeviceAlarmTriggerDTO> getAllDeviceAlarmTriggers() {
        return deviceAlarmTriggerRepository.findAll().stream()
                .map(t -> new DeviceAlarmTriggerDTO(t.getAlarmName(), t.getDeviceType(), t.getLowerThan(), t.getHigherThan(), t.getInvalidState(), t.getRuleName()))
                .collect(Collectors.toList());
    }

    @Override
    public void addNewTrigger(NewAlarmDeviceTrigger trigger) {
        validateNewTrigger(trigger);

        DeviceAlarmTrigger deviceAlarmTrigger = new DeviceAlarmTrigger();

        deviceAlarmTrigger.setAlarmName(trigger.getAlarmName());
        deviceAlarmTrigger.setDeviceType(trigger.getSelectedDevice());
        if (trigger.isHasState()) {
            deviceAlarmTrigger.setRuleName(trigger.getSelectedDevice() + " at invalid state: " + trigger.getState());
            deviceAlarmTrigger.setInvalidState(trigger.getState());
            deviceAlarmTrigger.setLowerThan(0.0);
            deviceAlarmTrigger.setHigherThan(0.0);
        } else {
            deviceAlarmTrigger.setInvalidState("");
            if (trigger.isGreater()) {
                deviceAlarmTrigger.setRuleName(trigger.getSelectedDevice() + " higher than " + trigger.getValue() + "°");
                deviceAlarmTrigger.setHigherThan(trigger.getValue());
                deviceAlarmTrigger.setLowerThan(0.0);
            } else {
                deviceAlarmTrigger.setRuleName(trigger.getSelectedDevice() + " lower than " + trigger.getValue() + "°");
                deviceAlarmTrigger.setHigherThan(0.0);
                deviceAlarmTrigger.setLowerThan(trigger.getValue());
            }
        }
        deviceAlarmTriggerRepository.save(deviceAlarmTrigger);
    }

    private void validateNewTrigger(NewAlarmDeviceTrigger trigger) {
        DeviceInfoDTO device = deviceService.getDeviceInfoByType(trigger.getSelectedDevice());
        if (device == null) {
            String errorMessage = "Can't find device type: " + trigger.getSelectedDevice();
            logService.addErr(new LogDTO(LogAction.UNKNOWN_DEVICE_TYPE, CLS_NAME, errorMessage));
            throw new BadRequestException(errorMessage);
        }
        if (trigger.isHasState() && device.getInvalidStates().stream().noneMatch(s -> s.equals(trigger.getState()))) {
            String errorMessage = "Invalid alarm state: " + trigger.getState();
            logService.addErr(new LogDTO(LogAction.UNKNOWN_ALARM_STATE, CLS_NAME, errorMessage));
            throw new BadRequestException(errorMessage);
        }
    }


}
