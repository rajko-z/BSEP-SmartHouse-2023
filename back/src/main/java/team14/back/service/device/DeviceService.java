package team14.back.service.device;

import team14.back.dto.ReportDataDTO;
import team14.back.dto.UpdateDeviceStateDTO;
import team14.back.dto.DeviceMessageDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface DeviceService {
    List<DeviceMessageDTO> getDeviceMessages(List<String> deviceMessagesPaths);

    void updateDeviceState(UpdateDeviceStateDTO newDeviceState);

    ReportDataDTO getReportData(String startDate, String endDate, List<String> deviceMessagesPaths);

    LocalDateTime customDateParse(String rawDate);

    String monthTransformer(String rawMonth);
}
