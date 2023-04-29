package team14.back.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginWith2FACodeDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String mfaCode;
}
