package team14.back.dto;
import lombok.*;

import java.security.cert.X509Certificate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CertificateDataDTO {
    private String alias;
    private String algorithm;
    private int keySize;
    private Date creationDate;
    private Date expiryDate;

    public CertificateDataDTO(String alias, X509Certificate certificate) {
        this.alias = alias;
        this.algorithm = certificate.getPublicKey().getAlgorithm();
        this.keySize = certificate.getPublicKey().getEncoded().length * 8;
        this.creationDate = certificate.getNotBefore();
        this.expiryDate = certificate.getNotAfter();
    }
}
