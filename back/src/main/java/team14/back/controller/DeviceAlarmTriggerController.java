package team14.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team14.back.dto.DeviceAlarmTriggerDTO;
import team14.back.dto.NewAlarmDeviceTrigger;
import team14.back.dto.TextResponse;
import team14.back.service.alarm.DeviceAlarmTriggerService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/device-alarm-triggers")
@AllArgsConstructor
public class DeviceAlarmTriggerController {

    private final DeviceAlarmTriggerService deviceAlarmTriggerService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<DeviceAlarmTriggerDTO>> getAll() {
        List<DeviceAlarmTriggerDTO> retVal = deviceAlarmTriggerService.getAllDeviceAlarmTriggers();
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<TextResponse> addNewAlarmTrigger(@RequestBody @Valid NewAlarmDeviceTrigger trigger) {
        deviceAlarmTriggerService.addNewTrigger(trigger);
        return new ResponseEntity<>(new TextResponse("Success"), HttpStatus.OK);
    }

}
