package team14.back.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Document("revoked_certificates")
public class RevokedCertificate {
    @Id
    private BigInteger serialNumber;
    private String reason;

    public RevokedCertificate() {

    }

    public RevokedCertificate(BigInteger serialNumber, String reason) {
        this.serialNumber = serialNumber;
        this.reason = reason;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
