package team14.back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team14.back.model.Facility;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FacilityDTO {
    @NotBlank
    @Max(256)
    private String name;
    @NotBlank
    @Max(256)
    @Pattern(regexp = "House|Apartment|Cottage")
    private String facilityType;
}