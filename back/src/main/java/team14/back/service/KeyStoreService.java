package team14.back.service;

import team14.back.model.IssuerData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;

public interface KeyStoreService {
    IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass);

    Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias);

    PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass);

    HashMap<String, X509Certificate> getAllCertificates() throws KeyStoreException;

    boolean isCertificateRevoked(X509Certificate certificate) throws CertificateException, IOException, CRLException;

    void addCertificateToCRL(X509Certificate certificate) throws IOException, CertificateException, CRLException;
}
