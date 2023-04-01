package team14.back.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CSRRequestData {

    public String commonName;
    public String organizationUnit;
    public String organizationName;
    public String localityName;
    public String stateName;
    public String countryName;
    public String publicKey;
    public String isValidSignature;
}
