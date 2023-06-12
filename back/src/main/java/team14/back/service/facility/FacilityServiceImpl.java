package team14.back.service.facility;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team14.back.dto.FacilityDetailsDTO;
import team14.back.dto.LogDTO;
import team14.back.enumerations.LogAction;
import team14.back.exception.NotFoundException;
import team14.back.model.Device;
import team14.back.model.Facility;
import team14.back.model.User;
import team14.back.repository.DeviceRepository;
import team14.back.repository.FacilityRepository;
import team14.back.repository.UserRepository;
import team14.back.service.log.LogService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private static final String CLS_NAME = FacilityServiceImpl.class.getName();

    private final DeviceRepository deviceRepository;

    private final UserRepository userRepository;

    private final FacilityRepository facilityRepository;

    private final LogService logService;

    @Override
    public List<Device> getAllDevices() {
        List<Device> allDevices = new ArrayList<>();
        List<User> users = userRepository.findAll().stream().filter(user -> !user.isDeleted() && user.isEnabled()).toList();
        for (User user: users) {
            if(user.getFacilities() != null) {
                for (Facility facility : user.getFacilities()) {
                    allDevices.addAll(deviceRepository.getDevicesFromFacility(facility.getName()));
                }
            }
        }
        logService.addInfo(new LogDTO(LogAction.GET_ALL_DEVICES, CLS_NAME, "Fetching all devices..."));
        return allDevices;
    }

    @Override
    public FacilityDetailsDTO getFacilityByName(String facilityName) {
        Facility facility = this.facilityRepository.findByName(facilityName).orElseThrow(()->new NotFoundException("Facility with name: " + facilityName + " not found!"));
        List<Device> devices = this.deviceRepository.getDevicesFromFacility(facilityName);
        return new FacilityDetailsDTO(facility, devices);
    }

    @Override
    public String getFacilityNameByDeviceId(Long deviceId) {
        List<User> users = userRepository.findAll().stream().filter(user -> !user.isDeleted() && user.isEnabled()).toList();
        for (User user: users) {
            if(user.getFacilities() != null) {
                for (Facility facility : user.getFacilities()) {
                    List<Device> devices = deviceRepository.getDevicesFromFacility(facility.getName());
                    if (devices.stream().filter(device -> Objects.equals(device.getId(), deviceId)).count() == 1)
                        return facility.getName();
                }
            }
        }
        return null;
    }

}
