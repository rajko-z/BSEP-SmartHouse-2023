package team14.back.service.crt;

import team14.back.dto.crt.NewCertificateDTO;

import javax.servlet.http.HttpServletRequest;

public interface CertificateCreationService {
    void issueNewCertificate(NewCertificateDTO certificateDTO, HttpServletRequest request);
}
