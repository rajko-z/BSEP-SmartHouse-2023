package team14.back.parseCSR;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCSException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ProbaParsingCSR {

    private static final String csrPEM = "-----BEGIN CERTIFICATE REQUEST-----\n"
            + "MIICxDCCAawCAQAwfzELMAkGA1UEBhMCVVMxETAPBgNVBAgMCElsbGlub2lzMRAw\n"
            + "DgYDVQQHDAdDaGljYWdvMQ4wDAYDVQQKDAVDb2RhbDELMAkGA1UECwwCTkExDjAM\n"
            + "BgNVBAMMBUNvZGFsMR4wHAYJKoZIhvcNAQkBFg9rYmF4aUBjb2RhbC5jb20wggEi\n"
            + "MA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDSrEF27VvbGi5x7LnPk4hRigAW\n"
            + "1feGeKOmRpHd4j/kUcJZLh59NHJHg5FMF7u9YdZgnMdULawFVezJMLSJYJcCAdRR\n"
            + "hSN+skrQlB6f5wgdkbl6ZfNaMZn5NO1Ve76JppP4gl0rXHs2UkRJeb8lguOpJv9c\n"
            + "tw+Sn6B13j8jF/m/OhIYI8fWhpBYvDXukgADTloCjOIsAvRonkIpWS4d014deKEe\n"
            + "5rhYX67m3H7GtZ/KVtBKhg44ntvuT2fR/wB1FlDws+0gp4edlkDlDml1HXsf4FeC\n"
            + "ogijo6+C9ewC2anpqp9o0CSXM6BT2I0h41PcQPZ4EtAc4ctKSlzTwaH0H9MbAgMB\n"
            + "AAGgADANBgkqhkiG9w0BAQsFAAOCAQEAqfQbrxc6AtjymI3TjN2upSFJS57FqPSe\n"
            + "h1YqvtC8pThm7MeufQmK9Zd+Lk2qnW1RyBxpvWe647bv5HiQaOkGZH+oYNxs1XvM\n"
            + "y5huq+uFPT5StbxsAC9YPtvD28bTH7iXR1b/02AK2rEYT8a9/tCBCcTfaxMh5+fr\n"
            + "maJtj+YPHisjxKW55cqGbotI19cuwRogJBf+ZVE/4hJ5w/xzvfdKjNxTcNr1EyBE\n"
            + "8ueJil2Utd1EnVrWbmHQqnlAznLzC5CKCr1WfmnrDw0GjGg1U6YpjKBTc4MDBQ0T\n"
            + "56ZL2yaton18kgeoWQVgcbK4MXp1kySvdWq0Bc3pmeWSM9lr/ZNwNQ==\n"
            + "-----END CERTIFICATE REQUEST-----\n";

    private static final String moj = "-----BEGIN CERTIFICATE REQUEST-----\n" +
            "MIICyDCCAbACAQAwgYIxCzAJBgNVBAYTAlJTMRMwEQYDVQQIDApzdGF0ZSBuYW1lMREwDwYDVQQH\n" +
            "DAhsb2NhbGl0eTEZMBcGA1UECgwQb3Jhbml6YXRpb24gbmFtZTERMA8GA1UECwwIb3JnIHVuaXQx\n" +
            "HTAbBgNVBAMMFHJhamtvemdyYzRAZ21haWwuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB\n" +
            "CgKCAQEAzRvHoNhQM/YR6H4wrSOvnRIHyYHWIB6h0WmWb399gpC+a3ER2JVrf8GF8sl9D2PcZs6q\n" +
            "t48viYFSS0y5KXCc+na3wOXgUg20UwB5hj8zxeIZbgORMP7bt77BygYkrepjYpoG5X4v953UKowL\n" +
            "EM66FttN+cXfcLdwmXdNeewGrJ4H96kDaNBb1GWkFN/eTRG7457jHZWeN/2KuhrVmcvI8eNu6481\n" +
            "ZG7Nvdj668k0oCaCI5Qka/LUzmydiMKudjjVFejkLy5n5S30rQyOzE7A14MsQczcJkqJ9luF1KKP\n" +
            "YnMk03pS4gRE7AB9mdCcjUF9LwlVSOLGerEQk0osw7E8qQIDAQABoAAwDQYJKoZIhvcNAQELBQAD\n" +
            "ggEBAD5pPeSeCfwg+O5sCfS/lHDQR7v0ysJWGsSxEolqXT8VPnUR2QpJDzbycxnE9HOQFNHbXUj4\n" +
            "uGUnHlO9XU/dssz7I9cCGpDgxOqUymY4wupUuV6nsxYC8PxNWXYymAttT0OWszu/4FxTT6A9XGaG\n" +
            "xS+DZgfHDdARX3g/CaDORr6+KO/ByBk/5iMhnWIDKAGwXVILun8ZWSLzpJe+rvH4iJUtjq41oceh\n" +
            "XiPxXT1fWvghsw19FdLhgvKavTz8Cn4Y7JHz1RXOBrm0NQpBlmYtJsIMWgXc/CFwIIhtVatRX16y\n" +
            "fz9vaYUg1tyVCYlMJiymZ8LUkkhsjLAHKInowJrI1Sg=\n" +
            "-----END CERTIFICATE REQUEST-----\n";

    private static final String COUNTRY = "2.5.4.6";
    private static final String STATE = "2.5.4.8";
    private static final String LOCALE = "2.5.4.7";
    private static final String ORGANIZATION = "2.5.4.10";
    private static final String ORGANIZATION_UNIT = "2.5.4.11";
    private static final String COMMON_NAME = "2.5.4.3";
    private static final String EMAIL = "2.5.4.9";

    public void test() throws IOException {
        File file = ResourceUtils.getFile("classpath:rajkotest.csr");

        try (final ByteArrayInputStream bais = new ByteArrayInputStream(moj.getBytes());
             final InputStreamReader isr = new InputStreamReader(bais, StandardCharsets.UTF_8);
             final PEMParser pem = new PEMParser(isr)) {
            PKCS10CertificationRequest csr = (PKCS10CertificationRequest) pem.readObject();
            // Do your verification here
            System.out.println(csr.getSubject());

            X500Name x500Name = csr.getSubject();

            System.out.println("x500Name is: " + x500Name + "\n");

            //RDN cn = x500Name.getRDNs(BCStyle.EmailAddress)[0];
            //System.out.println(cn.getFirst().getValue().toString());
            //System.out.println(x500Name.getRDNs(BCStyle.EmailAddress)[0]);
            System.out.println("COUNTRY: " + getX500Field(COUNTRY, x500Name));
            System.out.println("STATE: " + getX500Field(STATE, x500Name));
            System.out.println("LOCALE: " + getX500Field(LOCALE, x500Name));
            System.out.println("ORGANIZATION: " + getX500Field(ORGANIZATION, x500Name));
            System.out.println("ORGANIZATION_UNIT: " + getX500Field(ORGANIZATION_UNIT, x500Name));
            System.out.println("COMMON_NAME: " + getX500Field(COMMON_NAME, x500Name));
            System.out.println("EMAIL: " + getX500Field(EMAIL, x500Name));

            System.out.println(csr.getSubjectPublicKeyInfo());
            System.out.println(csr.getSubjectPublicKeyInfo().getPublicKeyData().toString());
            System.out.println(csr.getSubjectPublicKeyInfo().getAlgorithm().getAlgorithm().toString());
            CertificationRequestInfo c = csr.toASN1Structure().getCertificationRequestInfo();
            System.out.println(c.toString());
            System.out.println(c.getVersion().toString());
            System.out.println(c.getAttributes().toString());

            boolean isValid = isSignatureValidd(csr);
            System.out.println(isValid);
        } catch (OperatorCreationException e) {
            throw new RuntimeException(e);
        } catch (PKCSException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isSignatureValidd(PKCS10CertificationRequest csr)
            throws OperatorCreationException, PKCSException
    {
        // Implementation references:
        //  http://www.bouncycastle.org/wiki/display/JA1/BC+Version+2+APIs#BCVersion2APIs-VerifyingaSignature
        //  http://stackoverflow.com/questions/3711754/why-java-security-nosuchproviderexception-no-such-provider-bc
        JcaContentVerifierProviderBuilder builder =
                new JcaContentVerifierProviderBuilder().setProvider(new BouncyCastleProvider());
        return csr.isSignatureValid(builder.build(csr.getSubjectPublicKeyInfo()));
    }

    private String getX500Field(String asn1ObjectIdentifier, X500Name x500Name) {
        RDN[] rdnArray = x500Name.getRDNs(new ASN1ObjectIdentifier(asn1ObjectIdentifier));

        String retVal = null;
        for (RDN item : rdnArray) {
            retVal = item.getFirst().getValue().toString();
        }
        return retVal;
    }

    public static void main(String[] args) throws IOException {
        ProbaParsingCSR p = new ProbaParsingCSR();
        p.test();
    }
}
