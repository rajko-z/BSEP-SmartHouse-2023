package team14.back.service.crt.impl;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import team14.back.dto.crt.CertificateDataDTO;
import team14.back.dto.crt.VerifyCertificateResponseDTO;
import team14.back.model.RevokedCertificate;
import team14.back.repository.RevokedCertificateRepository;
import team14.back.service.crt.CertificateService;
import team14.back.service.keystore.KeyStoreService;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private final RevokedCertificateRepository revokedCertificateRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final KeyStoreService keyStoreService;

    @Override
    public List<String> getRevokedCertificatesSerialNumbers() {
        List<RevokedCertificate> revokedCertificates = this.revokedCertificateRepository.findAll();
        List<String> revokedCertificateSerialNumbers = new ArrayList<>();
        for (RevokedCertificate revokedCertificate : revokedCertificates)
        {
            revokedCertificateSerialNumbers.add(String.valueOf(revokedCertificate.getSerialNumber()));
        }
        return revokedCertificateSerialNumbers;
    }

    @Override
    public List<CertificateDataDTO> getAllCertificates() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchProviderException, CRLException {
        HashMap<String, X509Certificate> allCertificates = this.keyStoreService.getAllCertificates();
        List<CertificateDataDTO> certificateDataDTOS = new ArrayList<>();
        for(Map.Entry<String, X509Certificate> entry: allCertificates.entrySet()) {
            X509Certificate certificate = entry.getValue();
            certificateDataDTOS.add(new CertificateDataDTO(entry.getKey(), certificate, isCertificateValid(certificate)));
        }
        return certificateDataDTOS;
    }

    @Override
    public void verifyCertificate(BigInteger certificateSerialNumber) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, NoSuchProviderException, CRLException {
        X509Certificate certificateToCheck = findCertificateBySerialNumber(certificateSerialNumber);

        if (isCertificateValid(certificateToCheck))
        {
            simpMessagingTemplate.convertAndSend("/verify-certificate-response", new VerifyCertificateResponseDTO("success", "Certificate is valid"));
        }

        else
        {
            simpMessagingTemplate.convertAndSend("/verify-certificate-response", new VerifyCertificateResponseDTO("error", "Certificate is not valid"));
        }
    }

    @Override
    public void revokeCertificate(BigInteger certificateSerialNumber, String reasonForRevoking) throws KeyStoreException, CertificateException, IOException, CRLException {
        RevokedCertificate revokedCertificate = new RevokedCertificate(certificateSerialNumber, reasonForRevoking);
        revokedCertificateRepository.save(revokedCertificate);
    }

    @Override
    public X509Certificate findCertificateBySerialNumber(BigInteger serialNumber) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, NoSuchProviderException {
        HashMap<String, X509Certificate> allCertificates = this.keyStoreService.getAllCertificates();
        X509Certificate certificateToCheck = allCertificates.values().iterator().next();
        for(Map.Entry<String, X509Certificate> entry: allCertificates.entrySet()) {
            X509Certificate certificate = entry.getValue();
            if (certificate.getSerialNumber().equals(serialNumber)) {
                certificateToCheck = certificate;
                break;
            }
        }
        return certificateToCheck;
    }

    public boolean isCertificateValid(X509Certificate certificateToCheck) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, IOException, CRLException {
        boolean revoked = isCertificateRevoked(certificateToCheck);

        try {
            certificateToCheck.checkValidity();
            certificateToCheck.verify(certificateToCheck.getPublicKey());
        }
        catch (SignatureException | CertificateException e){
            return false;
        }
        return !revoked;
    }

    public boolean isCertificateRevoked(X509Certificate certificateToCheck)
    {
        List<RevokedCertificate> revokedCertificates = revokedCertificateRepository.findAll();
        for (RevokedCertificate revokedCertificate: revokedCertificates)
        {
            if (revokedCertificate.getSerialNumber().equals(certificateToCheck.getSerialNumber()))
            {
                return true;
            }
        }

        return false;
    }
}
