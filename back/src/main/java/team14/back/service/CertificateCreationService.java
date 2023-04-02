package team14.back.service;

import team14.back.dto.NewCertificateDTO;

public interface CertificateCreationService {
    void issueNewCertificate(NewCertificateDTO certificateDTO);
}
