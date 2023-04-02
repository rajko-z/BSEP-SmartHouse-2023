package team14.back.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class KeyUsageDTO {

    private boolean certificateSigning;
    private boolean decipherOnly;
    private boolean keyAgreement;
    private boolean crlSign;
    private boolean digitalSignature;
    private boolean keyEncipherment;
    private boolean dataEncipherment;
    private boolean encipherOnly;
    private boolean nonRepudiation;
}
