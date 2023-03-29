package team14.back.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team14.back.dto.CertificateDataDTO;
import team14.back.dto.RemovedCertificateDTO;
import team14.back.model.RemovedCertificate;
import team14.back.repository.RemovedCertificateRepository;
import team14.back.service.CertificateService;
import team14.back.service.KeyStoreService;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

@Service
@AllArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private final RemovedCertificateRepository removedCertificateRepository;

    private final KeyStoreService keyStoreService;

    @Override
    public List<RemovedCertificateDTO> getRemovedCertificates() {
        List<RemovedCertificate> removedCertificates = this.removedCertificateRepository.findAll();
        List<RemovedCertificateDTO> removedCertificateDTOS = new ArrayList<>();
        for (RemovedCertificate removedCertificate: removedCertificates)
        {
            removedCertificateDTOS.add(new RemovedCertificateDTO(removedCertificate.getCertificateAlias(), removedCertificate.getReason()));
        }
        return removedCertificateDTOS;
    }

    @Override
    public List<CertificateDataDTO> getAllCertificates() throws KeyStoreException {
        HashMap<String, X509Certificate> allCertificates = this.keyStoreService.getAllCertificates();
        List<CertificateDataDTO> certificateDataDTOS = new ArrayList<>();
        for(Map.Entry<String, X509Certificate> entry: allCertificates.entrySet()) {
            X509Certificate certificate = entry.getValue();
            certificateDataDTOS.add(new CertificateDataDTO(entry.getKey(), certificate));
        }
        return certificateDataDTOS;
    }

    @Override
    public void verifyCertificate(BigInteger certificateSerialNumber) throws KeyStoreException, CertificateNotYetValidException, CertificateExpiredException {
        HashMap<String, X509Certificate> allCertificates = this.keyStoreService.getAllCertificates();
        X509Certificate certificateToCheck = allCertificates.values().iterator().next();
        for(Map.Entry<String, X509Certificate> entry: allCertificates.entrySet()) {
            X509Certificate certificate = entry.getValue();
            if (certificate.getSerialNumber().equals(certificateSerialNumber)) {
                certificateToCheck = certificate;
                break;
            }
        }

        certificateToCheck.checkValidity(new Date());
        boolean revoked = isCertificateRevoked(certificateToCheck);

//        if(driver.isPresent()){
//            simpMessagingTemplate.convertAndSendToUser(request.getInitiator().getEmail(), "/response-to-client", new ResponseToIniciatorDto("driverFound", "Driver " + driver.get().getEmail() + " has been found for your ride request."));
//        }
    }

    public boolean isCertificateRevoked(X509Certificate certificate, X509CRL crl) {
        try {
            // Check if the certificate is listed in the CRL
            return crl.isRevoked(certificate);
        } catch (Exception e) {
            // If any exceptions occur during the validation process,
            // the certificate is considered invalid
            return false;
        }
    }
}
