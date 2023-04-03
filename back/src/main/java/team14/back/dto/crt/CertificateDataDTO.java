package team14.back.dto.crt;
import lombok.*;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CertificateDataDTO {
    private BigInteger serialNumber;
    private String alias;
    private String algorithm;
    private int keySize;
    private Date creationDate;
    private Date expiryDate;
    private boolean isValid;

    public CertificateDataDTO(String alias, X509Certificate certificate, boolean isValid) {
        this.serialNumber = certificate.getSerialNumber();
        this.alias = alias;
        this.algorithm = certificate.getPublicKey().getAlgorithm();
        this.keySize = certificate.getPublicKey().getEncoded().length * 8;
        this.creationDate = certificate.getNotBefore();
        this.expiryDate = certificate.getNotAfter();
        this.isValid = isValid;
    }
}
