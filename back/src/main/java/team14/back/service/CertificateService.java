package team14.back.service;

import team14.back.dto.RemovedCertificateDTO;

import java.util.List;

public interface CertificateService {
    List<RemovedCertificateDTO> getRemovedCertificates();
}
