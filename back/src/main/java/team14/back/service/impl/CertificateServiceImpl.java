package team14.back.service.impl;

import lombok.AllArgsConstructor;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.stereotype.Service;
import team14.back.dto.CertificateDataDTO;
import team14.back.dto.RemovedCertificateDTO;
import team14.back.model.IssuerData;
import team14.back.model.RemovedCertificate;
import team14.back.repository.RemovedCertificateRepository;
import team14.back.service.CertificateService;
import team14.back.service.KeyStoreService;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private final RemovedCertificateRepository removedCertificateRepository;

    private final KeyStoreService keyStoreService;

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

    @Override
    public List<CertificateDataDTO> getAllCertificates() throws KeyStoreException {
        HashMap<String, X509Certificate> allCertificates = this.keyStoreService.getAllCertificates();
        List<CertificateDataDTO> certificateDataDTOS = new ArrayList<>();
        for(Map.Entry<String, X509Certificate> entry: allCertificates.entrySet()) {
            X509Certificate certificate = entry.getValue();
            certificateDataDTOS.add(new CertificateDataDTO(entry.getKey(), certificate));
        }
        return certificateDataDTOS;
    }
}
