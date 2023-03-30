package team14.back.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RemovedCertificateDTO {
    private String certificateAlias;
    private String reason;
}
