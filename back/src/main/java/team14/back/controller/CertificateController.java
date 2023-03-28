package team14.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team14.back.dto.RemovedCertificateDTO;
import team14.back.service.CertificateService;

import java.util.List;

@RestController
@RequestMapping("/certificates")
@AllArgsConstructor
public class CertificateController {
    private final CertificateService certificateService;

//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OWNER')")
    @GetMapping("get-removed-certificates")
    public List<RemovedCertificateDTO> getRemovedCertificates() {
        return certificateService.getRemovedCertificates();
    }
}
