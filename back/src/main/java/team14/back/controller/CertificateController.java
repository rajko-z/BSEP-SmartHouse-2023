package team14.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team14.back.dto.CertificateDataDTO;
import team14.back.dto.RemovedCertificateDTO;
import team14.back.service.CertificateService;

import java.math.BigInteger;
import java.security.KeyStoreException;
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

    @GetMapping("get-all-certificates")
    public List<CertificateDataDTO> getAllCertificates() throws KeyStoreException {
        return certificateService.getAllCertificates();
    }

    @PostMapping("verify-certificate")
    public ResponseEntity<?> verifyCertificate(@RequestBody BigInteger certificateSerialNumber)
    {
        this.certificateService.verifyCertificate(certificateSerialNumber);
        return ResponseEntity.ok("Success!");
    }
}
