package team14.back.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SubjectDataDTO {
    @NotBlank
    private String email;
    private String commonName;
    private String organizationUnit;
    private String organizationName;
    private String localityName;
    private String stateName;
    private String countryName;
}
