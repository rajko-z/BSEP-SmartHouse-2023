package team14.back.service.facility;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team14.back.model.Device;
import team14.back.model.Facility;
import team14.back.model.User;
import team14.back.repository.DeviceRepository;
import team14.back.repository.FacilityRepository;
import team14.back.repository.UserRepository;

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
            if(user.getFacilities() != null) {
                for (Facility facility : user.getFacilities()) {
                    allDevices.addAll(deviceRepository.getDevicesFromFacility(facility.getName()));
                }
            }
        }
        return allDevices;
    }


}
