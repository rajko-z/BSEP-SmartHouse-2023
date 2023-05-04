package team14.back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangeRoleDto {
    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "ROLE_OWNER|ROLE_TENANT")
    private String newRole;
}
