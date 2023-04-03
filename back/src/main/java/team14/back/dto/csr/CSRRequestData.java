package team14.back.dto.csr;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CSRRequestData {

    private String commonName;
    private String organizationUnit;
    private String organizationName;
    private String localityName;
    private String stateName;
    private String countryName;
    private String publicKey;
    private String signature;
    private boolean isValidSignature;
}
