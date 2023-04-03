package team14.back.dto.crt;

import lombok.*;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RevokedCertificateDTO {
    private BigInteger serialNumber;
    private String reason;
}
