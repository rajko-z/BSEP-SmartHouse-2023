package team14.back.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import team14.back.dto.FacilityDTO;
import team14.back.enumerations.FacilityType;

import java.util.ArrayList;
import java.util.List;

@Document("facilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Facility {
    @Id
    private String name;

    private FacilityType facilityType;

    private String address;

    private User owner;

    @DBRef
    private List<User> tenants;

    private String configFilePath;

    public Facility(FacilityDTO facilityDTO, User owner, List<User> tenants){
        this.name = facilityDTO.getName();
        this.facilityType = FacilityType.valueOf(facilityDTO.getFacilityType().toUpperCase());
        this.address = facilityDTO.getAddress();
        this.owner = owner;
        this.tenants = tenants;
    }
}
