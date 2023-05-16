package team14.back.controller;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team14.back.dto.DeviceMessageDTO;
import team14.back.dto.FacilityDetailsDTO;
import team14.back.service.device.DeviceService;
import team14.back.service.facility.FacilityService;

import javax.validation.constraints.Email;
import java.util.List;

@RestController
@RequestMapping("/devices")
@AllArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping()
    public ResponseEntity<List<DeviceMessageDTO>> getDeviceMessages(
            @RequestParam("deviceMessagesPaths") List<String> deviceMessagesPaths
    ) {
        List<DeviceMessageDTO> deviceMessageDTO = this.deviceService.getDeviceMessages(deviceMessagesPaths);
        return ResponseEntity.ok(deviceMessageDTO);
    }
}
