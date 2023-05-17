package team14.back.controller;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team14.back.dto.FacilityDetailsDTO;
import team14.back.service.facility.FacilityService;

@RestController
@RequestMapping("/facilities")
@AllArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OWNER')")
    @GetMapping("/{facilityName}")
    public ResponseEntity<?> getFacilityByName(@PathVariable String facilityName){
        FacilityDetailsDTO facilityDetailsDTO = this.facilityService.getFacilityByName(facilityName);
        return new ResponseEntity<>(facilityDetailsDTO, HttpStatus.OK);
    }
}
