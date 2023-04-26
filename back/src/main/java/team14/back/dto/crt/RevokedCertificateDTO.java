package team14.back.dto.crt;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RevokedCertificateDTO {
    @NotBlank
    @Positive
    private BigInteger serialNumber;
    @NotBlank
    private String reason;
}
