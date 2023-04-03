package team14.back.service.keystore;

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
    IssuerData readIssuerFromStore(String alias, char[] password, char[] keyPass);

    Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias);

    PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass);

    HashMap<String, X509Certificate> getAllCertificates() throws KeyStoreException;

    void saveCertificate(Certificate certificate, String email);
}