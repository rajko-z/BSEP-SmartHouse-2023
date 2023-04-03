package team14.back.service.crt.impl;

import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;
import team14.back.dto.crt.KeyUsageDTO;
import team14.back.dto.LoginDTO;
import team14.back.dto.crt.NewCertificateDTO;
import team14.back.enumerations.IntermediateCA;
import team14.back.enumerations.TemplateType;
import team14.back.exception.InternalServerException;
import team14.back.model.IssuerData;
import team14.back.service.crt.CertDataValidationService;
import team14.back.service.crt.CertificateCreationService;
import team14.back.service.csr.CSRRequestReader;
import team14.back.service.csr.CSRRequestService;
import team14.back.service.email.EmailService;
import team14.back.service.keystore.KeyStoreService;
import team14.back.service.user.UserService;
import team14.back.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.ZoneId;
import java.util.Date;

@Service
@AllArgsConstructor
public class CertificateCreationServiceImpl implements CertificateCreationService {

    private final CertDataValidationService certDataValidationService;

    private final KeyStoreService keyStoreService;

    private final CSRRequestReader csrRequestReader;

    private final CSRRequestService csrRequestService;

    private final UserService userService;

    private final EmailService emailService;

    @Override
    public void issueNewCertificate(NewCertificateDTO certificateDTO) {
        String email = certificateDTO.getSubjectData().getEmail();

        certDataValidationService.validateNewCertificateData(certificateDTO);

        X509Certificate certificate = generateCertificate(certificateDTO);

        saveCertificateAsCertFile(certificate, email);

        LoginDTO createdCredentials = userService.createNewUser(email);

        emailService.sendCreatedCertificateAndPasswordToUser(createdCredentials);

        keyStoreService.saveCertificate(certificate, email);

        csrRequestService.deleteCSRRequest(email);

        System.out.println(certificate.toString());
    }

    private void saveCertificateAsCertFile(X509Certificate certificate, String email) {
        try {
            String encodedCert = "-----BEGIN CERTIFICATE-----\n" +
                    new String(Base64.encodeBase64(certificate.getEncoded())) +
                    "\n-----END CERTIFICATE-----\n";

            String filePath = "src/main/resources/data/crt/" + email + ".crt";
            File path = new File(filePath);
            path.createNewFile();
            try (PrintWriter out = new PrintWriter(new FileOutputStream(path))) {
                out.print(encodedCert);
            }
        } catch (CertificateEncodingException | IOException e) {
            throw new InternalServerException("Error happened while saving generated certificate");
        }
    }

    private X509Certificate generateCertificate(NewCertificateDTO certificateDTO) {
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            builder = builder.setProvider("BC");

            IssuerData issuerData = getIssuerData(certificateDTO);
            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

            BigInteger serialNumber = generateSerialNumber();
            Date startDate = new Date();
            Date endDate = Date.from(certificateDTO.getValidUntil().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            PublicKey subjectPublicKey = readPublicKeyForSubject(certificateDTO.getSubjectData().getEmail());
            X500Name subjectX500Name = createX500NameForSubject(certificateDTO);

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
                    issuerData.getX500name(),
                    serialNumber,
                    startDate,
                    endDate,
                    subjectX500Name,
                    subjectPublicKey);

            KeyUsage usage = createKeyUsage(certificateDTO.getKeyUsage());
            certGen.addExtension(Extension.keyUsage, false, usage);

            ExtendedKeyUsage extendedKeyUsage = createExtendedKeyUsage(certificateDTO.getTemplateType());
            certGen.addExtension(Extension.extendedKeyUsage, false, extendedKeyUsage);

            X509CertificateHolder certHolder = certGen.build(contentSigner);
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            return certConverter.getCertificate(certHolder);

        } catch (IllegalArgumentException |
                 IllegalStateException |
                 OperatorCreationException |
                 IOException |
                 NoSuchAlgorithmException |
                 InvalidKeySpecException |
                 CertificateException e) {

            e.printStackTrace();
            throw new InternalServerException("Error happened while generating certificate");
        }
    }

    private IssuerData getIssuerData(NewCertificateDTO certificateDTO) {
        if (certificateDTO.getIntermediateCA().equals(IntermediateCA.SMARTHOUSE2023FirstICA)) {
            return keyStoreService.readIssuerFromStore(Constants.INTERMEDIATE_FIRST_ICA, "admin".toCharArray(), "admin".toCharArray());
        }
        return keyStoreService.readIssuerFromStore(Constants.INTERMEDIATE_SECOND_ICA, "admin".toCharArray(), "admin".toCharArray());
    }

    private BigInteger generateSerialNumber() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[4];
        random.nextBytes(bytes);
        return new BigInteger(1, bytes);
    }

    private PublicKey readPublicKeyForSubject(String email) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS10CertificationRequest csr = csrRequestReader.readCSRForEmail(email);
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(csr.getSubjectPublicKeyInfo().getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(pubKeySpec);
    }

    private X500Name createX500NameForSubject(NewCertificateDTO newCertificateDTO) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, newCertificateDTO.getSubjectData().getCommonName());
        builder.addRDN(BCStyle.O, newCertificateDTO.getSubjectData().getOrganizationName());
        builder.addRDN(BCStyle.OU, newCertificateDTO.getSubjectData().getOrganizationUnit());
        builder.addRDN(BCStyle.L, newCertificateDTO.getSubjectData().getLocalityName());
        builder.addRDN(BCStyle.ST, newCertificateDTO.getSubjectData().getStateName());
        builder.addRDN(BCStyle.C, newCertificateDTO.getSubjectData().getCountryName());
        builder.addRDN(BCStyle.E, newCertificateDTO.getSubjectData().getEmail());
        return builder.build();
    }

    private KeyUsage createKeyUsage(KeyUsageDTO keyUsageDTO) {
        int i = 0;
        if (keyUsageDTO.isKeyAgreement())
            i = i | KeyUsage.keyAgreement;
        if (keyUsageDTO.isKeyEncipherment())
            i = i | KeyUsage.keyEncipherment;
        if (keyUsageDTO.isDecipherOnly())
            i = i | KeyUsage.decipherOnly;
        if (keyUsageDTO.isDataEncipherment())
            i = i | KeyUsage.dataEncipherment;
        if (keyUsageDTO.isNonRepudiation())
            i = i | KeyUsage.nonRepudiation;
        if (keyUsageDTO.isCrlSign())
            i = i | KeyUsage.cRLSign;
        if (keyUsageDTO.isDigitalSignature())
            i = i | KeyUsage.digitalSignature;
        if (keyUsageDTO.isEncipherOnly())
            i = i | KeyUsage.encipherOnly;
        if (keyUsageDTO.isCertificateSigning())
            i = i | KeyUsage.keyCertSign;

        return new KeyUsage(i);
    }

    private ExtendedKeyUsage createExtendedKeyUsage(TemplateType templateType) {
        if (templateType.equals(TemplateType.SSL_SERVER)) {
            return new ExtendedKeyUsage(new KeyPurposeId[] { KeyPurposeId.id_kp_serverAuth});
        }
        return new ExtendedKeyUsage(new KeyPurposeId[] { KeyPurposeId.id_kp_clientAuth});
    }
}
