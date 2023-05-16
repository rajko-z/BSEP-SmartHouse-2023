package team14.back.service.facility;

import team14.back.dto.DeviceMessageDTO;
import team14.back.dto.FacilityDetailsDTO;
import team14.back.model.Device;
import team14.back.model.Facility;
import team14.back.model.User;

import java.util.List;

public interface FacilityService {

    public List<Device> getAllDevices();

    public FacilityDetailsDTO getFacilityByName(String facilityName);

    String getFacilityNameByDeviceId(Long deviceId);
}
