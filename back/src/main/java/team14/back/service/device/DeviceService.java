package team14.back.service.device;

import team14.back.dto.DeviceInfoDTO;
import team14.back.dto.DeviceMessageDTO;
import team14.back.dto.ReportDataDTO;
import team14.back.dto.UpdateDeviceStateDTO;
import team14.back.enumerations.DeviceType;
import team14.back.model.Device;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface DeviceService {
    List<DeviceMessageDTO> getDeviceMessages(List<String> deviceMessagesPaths);

    List<DeviceInfoDTO> getAllDeviceInfos();

    DeviceInfoDTO getDeviceInfoByType(DeviceType type);

    void updateDeviceState(UpdateDeviceStateDTO newDeviceState, HttpServletRequest request);

    Device findDeviceById(long id);

    boolean isValidRegex(String pattern);

    ReportDataDTO getReportData(String startDate, String endDate, List<String> deviceMessagesPaths);

    LocalDateTime customDateParse(String rawDate);

    String monthTransformer(String rawMonth);
}
