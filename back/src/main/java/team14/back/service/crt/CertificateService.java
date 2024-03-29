package team14.back.service.crt;

import team14.back.dto.crt.CertificateDataDTO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.util.List;

public interface CertificateService {
    List<String> getRevokedCertificatesSerialNumbers(HttpServletRequest request);

    List<CertificateDataDTO> getAllCertificates(HttpServletRequest request) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchProviderException, CRLException;

    void verifyCertificate(BigInteger certificateSerialNumber, HttpServletRequest request) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, NoSuchProviderException, CRLException;

    void revokeCertificate(BigInteger certificateSerialNumber, String reasonForRevoking, HttpServletRequest request) throws KeyStoreException, CertificateException, IOException, CRLException;

    X509Certificate findCertificateBySerialNumber(BigInteger serialNumber) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, NoSuchProviderException;
}
