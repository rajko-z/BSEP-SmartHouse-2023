package team14.back.service.keystore;

import team14.back.model.IssuerData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;

public interface KeyStoreService {
    IssuerData readIssuerFromStore(String alias, char[] password, char[] keyPass);

    Certificate readCertificate(String keyStorePass, String alias);

    HashMap<String, X509Certificate> getAllCertificates() throws KeyStoreException, NoSuchProviderException, IOException, CertificateException, NoSuchAlgorithmException;

    void saveCertificate(Certificate certificate, String email);
}
