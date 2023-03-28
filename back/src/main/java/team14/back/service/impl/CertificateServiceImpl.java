package team14.back.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team14.back.dto.RemovedCertificateDTO;
import team14.back.model.RemovedCertificate;
import team14.back.repository.RemovedCertificateRepository;
import team14.back.service.CertificateService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private final RemovedCertificateRepository removedCertificateRepository;

    @Override
    public List<RemovedCertificateDTO> getRemovedCertificates() {
        List<RemovedCertificate> removedCertificates = this.removedCertificateRepository.findAll();
        List<RemovedCertificateDTO> removedCertificateDTOS = new ArrayList<>();
        for (RemovedCertificate removedCertificate: removedCertificates)
        {
            removedCertificateDTOS.add(new RemovedCertificateDTO(removedCertificate.getCertificateAlias(), removedCertificate.getReason()));
        }
        return removedCertificateDTOS;
    }
}
