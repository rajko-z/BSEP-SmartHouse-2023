package team14.back.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team14.back.model.Facility;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddUserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;
    private List<FacilityDTO> facilities;
}