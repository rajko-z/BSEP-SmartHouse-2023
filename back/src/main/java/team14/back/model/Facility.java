package team14.back.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import team14.back.enumerations.FacilityType;

import java.util.List;

@Document("facilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Facility {
    @Id
    private String name;

    private FacilityType facilityType;

    private String address;

    private User owner;

    private List<User> tenants;
}
