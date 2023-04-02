package team14.back.service;

import team14.back.dto.NewCertificateDTO;

public interface CertDataValidationService {

    void validateNewCertificateData(NewCertificateDTO certificate);
}
