package team14.back.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import team14.back.dto.CertificateDataDTO;
import team14.back.dto.RemovedCertificateDTO;
import team14.back.dto.VerifyCertificateResponseDTO;
import team14.back.model.RemovedCertificate;
import team14.back.repository.RemovedCertificateRepository;
import team14.back.service.CertificateService;
import team14.back.service.KeyStoreService;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

@Service
@AllArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private final RemovedCertificateRepository removedCertificateRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

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
    public void verifyCertificate(BigInteger certificateSerialNumber) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, NoSuchProviderException, CRLException {
        HashMap<String, X509Certificate> allCertificates = this.keyStoreService.getAllCertificates();
        X509Certificate certificateToCheck = allCertificates.values().iterator().next();
        for(Map.Entry<String, X509Certificate> entry: allCertificates.entrySet()) {
            X509Certificate certificate = entry.getValue();
            if (certificate.getSerialNumber().equals(certificateSerialNumber)) {
                certificateToCheck = certificate;
                break;
            }
        }

        if (isCertificateValid(certificateToCheck))
        {
            simpMessagingTemplate.convertAndSend("/verify-certificate-response", new VerifyCertificateResponseDTO("success", "Certificate is valid"));
        }

        else
        {
            simpMessagingTemplate.convertAndSend("/verify-certificate-response", new VerifyCertificateResponseDTO("error", "Certificate is not valid"));
        }
    }

    public boolean isCertificateValid(X509Certificate certificateToCheck) throws CertificateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException, IOException, CRLException {
        boolean revoked = keyStoreService.isCertificateRevoked(certificateToCheck);

        try {
            certificateToCheck.checkValidity();
            certificateToCheck.verify(certificateToCheck.getPublicKey());
        } catch (CertificateException e) {
            return false;
        }

        return !revoked;
    }
}
