package team14.back.service;

import team14.back.dto.CertificateDataDTO;
import team14.back.dto.RemovedCertificateDTO;

import java.security.KeyStoreException;
import java.util.List;

public interface CertificateService {
    List<RemovedCertificateDTO> getRemovedCertificates();

    List<CertificateDataDTO> getAllCertificates() throws KeyStoreException;
}
