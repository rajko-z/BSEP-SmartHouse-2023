package team14.back.controller;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team14.back.dto.DeviceMessageDTO;
import team14.back.service.device.DeviceService;
import java.util.List;

@RestController
@RequestMapping("/devices")
@AllArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OWNER')")
    @GetMapping()
    public ResponseEntity<List<DeviceMessageDTO>> getDeviceMessages(
            @RequestParam("deviceMessagesPaths") List<String> deviceMessagesPaths
    ) {
        List<DeviceMessageDTO> deviceMessageDTO = this.deviceService.getDeviceMessages(deviceMessagesPaths);
        return ResponseEntity.ok(deviceMessageDTO);
    }
}
