package team14.back.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginWith2FACodeDto {

    @NotBlank
    @Email
    private String email;

    @Length(min=8)
    @Length(max=256)
    private String password;

    @Length(min = 6)
    @Length(max = 6)
    private String mfaCode;
}
