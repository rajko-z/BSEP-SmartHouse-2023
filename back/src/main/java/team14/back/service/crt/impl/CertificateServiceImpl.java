package team14.back.service.crt.impl;

import lombok.AllArgsConstructor;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import team14.back.dto.LogDTO;
import team14.back.dto.crt.CertificateDataDTO;
import team14.back.dto.crt.VerifyCertificateResponseDTO;
import team14.back.enumerations.LogAction;
import team14.back.model.RevokedCertificate;
import team14.back.repository.RevokedCertificateRepository;
import team14.back.service.crt.CertificateService;
import team14.back.service.keystore.KeyStoreService;
import team14.back.service.keystore.KeyStoreServiceImpl;
import team14.back.service.log.LogService;
import team14.back.utils.Constants;
import team14.back.utils.HttpUtils;

import javax.security.auth.x500.X500Principal;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final static String CLS_NAME = CertificateServiceImpl.class.getName();

    private final RevokedCertificateRepository revokedCertificateRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final KeyStoreService keyStoreService;

    private final LogService logService;

    @Override
    public List<String> getRevokedCertificatesSerialNumbers(HttpServletRequest request) {
        List<RevokedCertificate> revokedCertificates = this.revokedCertificateRepository.findAll();
        List<String> revokedCertificateSerialNumbers = new ArrayList<>();
        for (RevokedCertificate revokedCertificate : revokedCertificates)
        {
            revokedCertificateSerialNumbers.add(String.valueOf(revokedCertificate.getSerialNumber()));
        }
        logService.addInfo(new LogDTO(LogAction.GET_ALL_REVOKED_CERTIFICATES, CLS_NAME, "Getting all revoked certificate serial numbers", HttpUtils.getRequestIP(request)));
        return revokedCertificateSerialNumbers;
    }

    @Override
    public List<CertificateDataDTO> getAllCertificates(HttpServletRequest request) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchProviderException, CRLException {
        HashMap<String, X509Certificate> allCertificates = this.keyStoreService.getAllCertificates();
        List<CertificateDataDTO> certificateDataDTOS = new ArrayList<>();
        for(Map.Entry<String, X509Certificate> entry: allCertificates.entrySet()) {
            X509Certificate certificate = entry.getValue();
            certificateDataDTOS.add(new CertificateDataDTO(entry.getKey(), certificate, isCertificateValid(certificate)));
        }
        logService.addInfo(new LogDTO(LogAction.GET_ALL_CERTIFICATES, CLS_NAME, "Getting all certificates", HttpUtils.getRequestIP(request)));
        return certificateDataDTOS;
    }

    @Override
    public void verifyCertificate(BigInteger certificateSerialNumber, HttpServletRequest request) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, NoSuchProviderException, CRLException {
        X509Certificate certificateToCheck = findCertificateBySerialNumber(certificateSerialNumber);

        if (isCertificateValid(certificateToCheck))
        {
            logService.addInfo(new LogDTO(LogAction.VERIFY_CERTIFICATE, CLS_NAME, "Certificate verified.", HttpUtils.getRequestIP(request)));
            simpMessagingTemplate.convertAndSend("/verify-certificate-response", new VerifyCertificateResponseDTO("success", "Certificate is valid"));
        }

        else
        {
            logService.addErr(new LogDTO(LogAction.INVALID_CERTIFICATE, CLS_NAME, "Certificate invalid.", HttpUtils.getRequestIP(request)));
            simpMessagingTemplate.convertAndSend("/verify-certificate-response", new VerifyCertificateResponseDTO("error", "Certificate is not valid"));
        }
    }

    @Override
    public void revokeCertificate(BigInteger certificateSerialNumber, String reasonForRevoking, HttpServletRequest request) throws KeyStoreException, CertificateException, IOException, CRLException {
        RevokedCertificate revokedCertificate = new RevokedCertificate(certificateSerialNumber, reasonForRevoking);
        logService.addInfo(new LogDTO(LogAction.REVOKE_CERTIFICATE, CLS_NAME, "Revoking certificate", HttpUtils.getRequestIP(request)));
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
            certificateToCheck.verify(getPublicKeyFromIssuer(certificateToCheck));
        }
        catch (SignatureException | CertificateException e){
            return false;
        }
        return !revoked;
    }

    private PublicKey getPublicKeyFromIssuer(X509Certificate certificate) {
        String issuerName = certificate.getIssuerX500Principal().getName(X500Principal.RFC2253).split(",")[0].substring(3);
        if (issuerName.contains("First")) {
            return keyStoreService
                    .readCertificate("admin", Constants.INTERMEDIATE_FIRST_ICA).getPublicKey();
        } else if (issuerName.contains("Second")) {
            return keyStoreService
                    .readCertificate("admin", Constants.INTERMEDIATE_SECOND_ICA).getPublicKey();
        }
        return certificate.getPublicKey();
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
