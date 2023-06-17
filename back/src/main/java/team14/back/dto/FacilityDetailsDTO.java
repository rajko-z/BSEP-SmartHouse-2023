package team14.back.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import team14.back.enumerations.DeviceType;
import team14.back.model.Device;
import team14.back.model.Facility;
import team14.back.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FacilityDetailsDTO {
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

    @NotBlank
    @Length(max=256)
    private String owner;

    private List<String> tenantsEmails;

    private List<DeviceDTO> deviceDTOs;

    private String ownerEmail;

    public FacilityDetailsDTO(Facility facility, List<Device> devices){
        this.name = facility.getName();
        this.address = facility.getAddress();
        this.facilityType = facility.getFacilityType().name();
        this.ownerEmail = facility.getOwner().getEmail();
        this.owner = facility.getOwner().getFirstName() + " " + facility.getOwner().getLastName();
        this.tenantsEmails = new ArrayList<>();
        for (User tenant: facility.getTenants()) {
            this.tenantsEmails.add(tenant.getEmail());
        }
        this.deviceDTOs = new ArrayList<>();
        for(Device device: devices){
            this.deviceDTOs.add(new DeviceDTO(device.getId(), String.valueOf(device.getDeviceType()), device.getMessagesFilePath()));
        }
    }
}
