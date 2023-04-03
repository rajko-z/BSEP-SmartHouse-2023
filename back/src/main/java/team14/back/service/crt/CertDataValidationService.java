package team14.back.service.crt;

import team14.back.dto.crt.NewCertificateDTO;

public interface CertDataValidationService {

    void validateNewCertificateData(NewCertificateDTO certificate);
}
