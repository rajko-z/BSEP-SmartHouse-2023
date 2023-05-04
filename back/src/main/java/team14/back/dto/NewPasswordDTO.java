package team14.back.dto;

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
public class NewPasswordDTO {
    @NotBlank
    @Email
    private String email;

    private String currentPassword;
    //nisam stavljao validaciju ovde, jer smeta Rajkovom kodu za proveru nove sifre
    private String newPassword;
}