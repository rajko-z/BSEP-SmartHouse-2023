package team14.back.dto.crt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCertificateResponseDTO {
    private String messageType;
    private String messageContent;
}
