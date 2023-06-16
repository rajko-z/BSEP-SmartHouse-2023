package team14.back.service.alarm;

import team14.back.dto.UpdateDeviceStateDTO;
import team14.back.dto.alarms.ActivatedDeviceAlarmDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AlarmService {
    void checkForRedundantCSRRequest();

    void checkForDeviceAlarms(UpdateDeviceStateDTO newDeviceState, HttpServletRequest request);

    List<ActivatedDeviceAlarmDTO> getAllActivatedAlarms();

    List<ActivatedDeviceAlarmDTO> getAllActivatedAlarmsForFacility(String facility);
}
