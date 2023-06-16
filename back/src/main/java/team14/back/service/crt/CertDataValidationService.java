package team14.back.service.crt;

import team14.back.dto.crt.NewCertificateDTO;

import javax.servlet.http.HttpServletRequest;

public interface CertDataValidationService {

    void validateNewCertificateData(NewCertificateDTO certificate, HttpServletRequest request);
}
