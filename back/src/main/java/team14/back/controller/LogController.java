package team14.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team14.back.dto.LogDTO;
import team14.back.service.log.LogService;

import java.util.List;

@RestController
@RequestMapping("/logs")
@AllArgsConstructor
public class LogController {

    private final LogService logService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<LogDTO>> getAllLogs() {
        List<LogDTO> logs = logService.getAllLogs();
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

}
