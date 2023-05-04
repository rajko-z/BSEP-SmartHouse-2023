package team14.back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FacilityDTO {
    @NotBlank
    @Length(max=256)
    private String name;

    @NotBlank
    @Length(max=256)
    private String address;

    @NotBlank
    @Length(max=256)
    @Pattern(regexp = "House|Apartment|Cottage")
    private String facilityType;

    private List<String> tenantsEmails;
}