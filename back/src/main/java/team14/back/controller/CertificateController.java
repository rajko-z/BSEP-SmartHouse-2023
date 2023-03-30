package team14.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team14.back.dto.CertificateDataDTO;
import team14.back.dto.RemovedCertificateDTO;
import team14.back.service.CertificateService;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<CertificateDataDTO> getAllCertificates() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchProviderException, CRLException {
        return certificateService.getAllCertificates();
    }

    @PostMapping("verify-certificate")
    public ResponseEntity<?> verifyCertificate(@Param("certificateSerialNumber") String certificateSerialNumber) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, NoSuchProviderException, CRLException {
        this.certificateService.verifyCertificate(new BigInteger(certificateSerialNumber));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Success!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("revoke-certificate")
    public ResponseEntity<?> revokeCertificate(@Param("certificateSerialNumber") String certificateSerialNumber) throws CertificateException, KeyStoreException, NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, NoSuchProviderException, CRLException {
        this.certificateService.revokeCertificate(new BigInteger(certificateSerialNumber));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Success!");
        return ResponseEntity.ok(response);
    }
}
