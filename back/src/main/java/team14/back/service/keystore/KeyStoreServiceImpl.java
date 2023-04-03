package team14.back.service.keystore;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.stereotype.Service;
import team14.back.exception.BadRequestException;
import team14.back.exception.InternalServerException;
import team14.back.model.IssuerData;
import team14.back.service.csr.CSRRequestService;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;

@Service
public class KeyStoreServiceImpl implements KeyStoreService {

    private static final String SMARTHOUSE_CERT_STORE = "src/main/resources/data/p12/smarthouse_certstore.p12";

    private static final String CRL_FILE = "src/main/resources/revoked_certificates.crl";

    private final CSRRequestService csrRequestService;

    private KeyStore keyStore;

    public KeyStoreServiceImpl(CSRRequestService csrRequestService) {
        this.csrRequestService = csrRequestService;

        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IssuerData readIssuerFromStore(String alias, char[] password, char[] keyPass) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(SMARTHOUSE_CERT_STORE));
            keyStore.load(in, password);

            Certificate cert = keyStore.getCertificate(alias);

            PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, keyPass);

            X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
            return new IssuerData(privKey, issuerName);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException
                | UnrecoverableKeyException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ucitava sertifikat is KS fajla
     */
    @Override
    public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
        try {
            // kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            // ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if (ks.isKeyEntry(alias)) {
                Certificate cert = ks.getCertificate(alias);
                return cert;
            }
        } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException
                | CertificateException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ucitava privatni kljuc is KS fajla
     */
    @Override
    public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
        try {
            // kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            // ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if (ks.isKeyEntry(alias)) {
                PrivateKey pk = (PrivateKey) ks.getKey(alias, pass.toCharArray());
                return pk;
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | CertificateException
                | IOException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HashMap<String, X509Certificate> getAllCertificates() throws KeyStoreException {
        HashMap<String, X509Certificate> certificates = new HashMap<String, X509Certificate>();

        for (Iterator<String> it = keyStore.aliases().asIterator(); it.hasNext(); ) {
            String alias = it.next();
            X509Certificate certificate = (X509Certificate) readCertificate(SMARTHOUSE_CERT_STORE, "admin", alias);
            certificates.put(alias, certificate);
        }

        return certificates;
    }

    @Override
    public void saveCertificate(Certificate certificate, String email) {
        if (!csrRequestService.csrRequestForEmailExist(email)) {
            throw new BadRequestException("Can't save certificate because csr request is not present for email: " + email);
        }

        try {
            keyStore.setCertificateEntry(email, certificate);
            keyStore.store(new FileOutputStream(SMARTHOUSE_CERT_STORE), "admin".toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new InternalServerException("Error happened while saving certificate");
        }
    }
}
