package team14.back.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("removed_certificates")
public class RemovedCertificate {
    @Id
    private String certificateAlias;
    private String reason;

    public RemovedCertificate() {

    }

    public RemovedCertificate(String certificateAlias, String reason) {
        this.certificateAlias = certificateAlias;
        this.reason = reason;
    }

    public String getCertificateAlias() {
        return certificateAlias;
    }

    public void setCertificateAlias(String certificateAlias) {
        this.certificateAlias = certificateAlias;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
