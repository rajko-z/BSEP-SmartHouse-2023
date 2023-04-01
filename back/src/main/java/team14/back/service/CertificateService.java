package team14.back.service;

import team14.back.dto.CertificateDataDTO;
import team14.back.dto.RevokedCertificateDTO;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.util.List;

public interface CertificateService {
    List<String> getRevokedCertificatesSerialNumbers();

    List<CertificateDataDTO> getAllCertificates() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchProviderException, CRLException;

    void verifyCertificate(BigInteger certificateSerialNumber) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, NoSuchProviderException, CRLException;

    void revokeCertificate(BigInteger certificateSerialNumber, String reasonForRevoking) throws KeyStoreException, CertificateException, IOException, CRLException;

    X509Certificate findCertificateBySerialNumber(BigInteger serialNumber) throws KeyStoreException;
}
