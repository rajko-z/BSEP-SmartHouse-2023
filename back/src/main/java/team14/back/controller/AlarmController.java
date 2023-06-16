package team14.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team14.back.dto.alarms.ActivatedDeviceAlarmDTO;
import team14.back.service.alarm.AlarmService;

import java.util.List;

@RestController
@RequestMapping("/activated-alarms")
@AllArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping()
    public ResponseEntity<List<ActivatedDeviceAlarmDTO>> getAllActivatedAlarms() {
        List<ActivatedDeviceAlarmDTO> retVal = alarmService.getAllActivatedAlarms();
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }
}
