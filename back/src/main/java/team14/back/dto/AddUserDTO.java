package team14.back.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team14.back.model.Facility;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddUserDTO {
    @Email
    private String email;
    @NotBlank
    @Max(256)
    private String firstName;
    @NotBlank
    @Max(256)
    private String lastName;
    private String password;
    private String confirmPassword;
    private List<FacilityDTO> facilities;
}