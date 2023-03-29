package team14.back.service;

import team14.back.dto.CertificateDataDTO;
import team14.back.dto.RemovedCertificateDTO;

import java.math.BigInteger;
import java.security.KeyStoreException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.util.List;

public interface CertificateService {
    List<RemovedCertificateDTO> getRemovedCertificates();

    List<CertificateDataDTO> getAllCertificates() throws KeyStoreException;

    void verifyCertificate(BigInteger certificateSerialNumber) throws KeyStoreException, CertificateNotYetValidException, CertificateExpiredException;
}
