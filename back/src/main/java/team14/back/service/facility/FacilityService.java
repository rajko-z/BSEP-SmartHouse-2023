package team14.back.service.facility;

import team14.back.dto.FacilityDetailsDTO;
import team14.back.model.Device;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FacilityService {

    public List<Device> getAllDevices(HttpServletRequest request);

    public FacilityDetailsDTO getFacilityByName(String facilityName);

    String getFacilityNameByDeviceId(Long deviceId);
}
