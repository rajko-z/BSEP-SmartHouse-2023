package team14.back.dto.csr;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RejectCSRRequestDTO {

    @NotBlank
    @Length(max = 256)
    private String email;

    private String reason;
}
