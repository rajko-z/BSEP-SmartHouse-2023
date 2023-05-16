package team14.back.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team14.back.dto.AddUserDTO;
import team14.back.exception.BadRequestException;
import team14.back.model.Device;
import team14.back.service.facility.FacilityService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/device-script")
@AllArgsConstructor
public class DeviceScriptController {

    private final FacilityService facilityService;


    @GetMapping(path = "/get-all-devices", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> getAllDevices() {
        List<Device> deviceList = null;
        try{
            deviceList = facilityService.getAllDevices();
        } catch (BadRequestException e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(deviceList);
    }





}
