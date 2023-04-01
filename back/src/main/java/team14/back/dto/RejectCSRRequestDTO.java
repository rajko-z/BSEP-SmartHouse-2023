package team14.back.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RejectCSRRequestDTO {

    @NotBlank
    private String email;
    private String reason;
}
