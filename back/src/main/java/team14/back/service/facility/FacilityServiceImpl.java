package team14.back.service.facility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team14.back.dto.FacilityDetailsDTO;
import team14.back.exception.NotFoundException;
import team14.back.model.Device;
import team14.back.model.Facility;
import team14.back.model.User;
import team14.back.repository.DeviceRepository;
import team14.back.repository.FacilityRepository;
import team14.back.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private final DeviceRepository deviceRepository;

    private final UserRepository userRepository;

    private final FacilityRepository facilityRepository;

    @Override
    public List<Device> getAllDevices() {
        List<Device> allDevices = new ArrayList<>();
        List<User> users = userRepository.findAll().stream().filter(user -> !user.isDeleted() && user.isEnabled()).toList();
        for (User user :users) {
            for (Facility facility :user.getFacilities()) {
                allDevices.addAll(deviceRepository.getDevicesFromFacility(facility.getName()));
            }
        }
        return allDevices;
    }

    @Override
    public FacilityDetailsDTO getFacilityByName(String facilityName) {
        Facility facility = this.facilityRepository.findByName(facilityName).orElseThrow(()->new NotFoundException("Facility with name: " + facilityName + " not found!"));
        List<Device> devices = this.deviceRepository.getDevicesFromFacility(facilityName);
        return new FacilityDetailsDTO(facility, devices);
    }

}
