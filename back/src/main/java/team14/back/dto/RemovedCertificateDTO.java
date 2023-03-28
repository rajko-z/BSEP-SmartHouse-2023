package team14.back.dto;

public class RemovedCertificateDTO {
    private String certificateAlias;
    private String reason;

    public RemovedCertificateDTO(String certificateAlias, String reason) {
        this.certificateAlias = certificateAlias;
        this.reason = reason;
    }
}
