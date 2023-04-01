package team14.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team14.back.dto.SimpleCSRRequestDTO;
import team14.back.service.CSRRequestService;

import java.util.List;

@RestController
@RequestMapping("/csrrequests")
@AllArgsConstructor
public class CSRRequestController {

    private final CSRRequestService csrRequestService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<SimpleCSRRequestDTO> getAllSimpleCSRRequestsData() {
        return csrRequestService.getSimpleCSRRequestsData();
    }
}
