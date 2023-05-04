package team14.back.dto.csr;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CSRRequestData {

    @NotBlank
    @Length(max = 256)
    private String commonName;

    @NotBlank
    @Length(max = 256)
    private String organizationUnit;

    @NotBlank
    @Length(max = 256)
    private String organizationName;

    @NotBlank
    @Length(max = 256)
    private String localityName;

    @NotBlank
    @Length(max = 256)
    private String stateName;

    @NotBlank
    @Length(max = 256)
    private String countryName;

    @NotBlank
    private String publicKey;

    @NotBlank
    private String signature;

    private boolean isValidSignature;
}
