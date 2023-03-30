package team14.back.service;

import team14.back.dto.CertificateDataDTO;
import team14.back.dto.RemovedCertificateDTO;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.util.List;

public interface CertificateService {
    List<RemovedCertificateDTO> getRemovedCertificates();

    List<CertificateDataDTO> getAllCertificates() throws KeyStoreException;

    void verifyCertificate(BigInteger certificateSerialNumber) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, NoSuchProviderException, CRLException;
}
