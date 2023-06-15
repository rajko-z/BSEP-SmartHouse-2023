package team14.back.service.keystore;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.stereotype.Service;
import team14.back.dto.LogDTO;
import team14.back.enumerations.LogAction;
import team14.back.exception.BadRequestException;
import team14.back.exception.InternalServerException;
import team14.back.model.IssuerData;
import team14.back.service.csr.CSRRequestService;
import team14.back.service.log.LogService;
import team14.back.utils.Constants;
import team14.back.utils.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;

@Service
public class KeyStoreServiceImpl implements KeyStoreService {

    private static final String CLS_NAME = KeyStoreServiceImpl.class.getName();
    private static final String SMARTHOUSE_CERT_STORE = "src/main/resources/data/p12/smarthouse_certstore.p12";

    private final CSRRequestService csrRequestService;

    private final LogService logService;

    private KeyStore keyStore;

    public KeyStoreServiceImpl(CSRRequestService csrRequestService, LogService logService) {
        this.csrRequestService = csrRequestService;
        this.logService = logService;

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
    public Certificate readCertificate(String keyStorePass, String alias) {
        try {
            // kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            // ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(SMARTHOUSE_CERT_STORE));
            ks.load(in, keyStorePass.toCharArray());

            Certificate cert = ks.getCertificate(alias);
            return cert;
        } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException
                | CertificateException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HashMap<String, X509Certificate> getAllCertificates() throws KeyStoreException, NoSuchProviderException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore ks = KeyStore.getInstance("JKS", "SUN");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(SMARTHOUSE_CERT_STORE));
        ks.load(in, "admin".toCharArray());

        HashMap<String, X509Certificate> certificates = new HashMap<String, X509Certificate>();

        for (Iterator<String> it = ks.aliases().asIterator(); it.hasNext(); ) {
            String alias = it.next();
            if (alias.equals(Constants.INTERMEDIATE_FIRST_ICA) || alias.equals(Constants.INTERMEDIATE_SECOND_ICA) || alias.equals(Constants.ROOT_CA)) {
                continue;
            }
            X509Certificate certificate = (X509Certificate) readCertificate("admin", alias);
            certificates.put(alias, certificate);
        }

        return certificates;
    }

    @Override
    public void saveCertificate(Certificate certificate, String email, HttpServletRequest request) {
        if (!csrRequestService.csrRequestForEmailExist(email)) {
            String errorMessage = "Can't save certificate because csr request is not present for email: " + email;
            logService.addErr(new LogDTO(LogAction.ERROR_ON_STORING_CERTIFICATE, CLS_NAME, errorMessage, HttpUtils.getRequestIP(request)));
            throw new BadRequestException(errorMessage);
        }

        try {
            keyStore.setCertificateEntry(email, certificate);
            keyStore.store(new FileOutputStream(SMARTHOUSE_CERT_STORE), "admin".toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException | IOException | NoSuchAlgorithmException e) {
            String errorMessage = "Error happened while saving certificate for user: " + email;
            logService.addErr(new LogDTO(LogAction.ERROR_ON_STORING_CERTIFICATE, CLS_NAME, errorMessage, HttpUtils.getRequestIP(request)));
            throw new InternalServerException(errorMessage);
        }
    }
}
