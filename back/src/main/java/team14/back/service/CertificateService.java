package team14.back.service;

import team14.back.dto.CertificateDataDTO;
import team14.back.dto.RemovedCertificateDTO;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.util.List;

public interface CertificateService {
    List<RemovedCertificateDTO> getRemovedCertificates();

    List<CertificateDataDTO> getAllCertificates() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchProviderException, CRLException;

    void verifyCertificate(BigInteger certificateSerialNumber) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, NoSuchProviderException, CRLException;

    void revokeCertificate(BigInteger bigInteger) throws KeyStoreException, CertificateException, IOException, CRLException;

    X509Certificate findCertificateBySerialNumber(BigInteger serialNumber) throws KeyStoreException;
}
