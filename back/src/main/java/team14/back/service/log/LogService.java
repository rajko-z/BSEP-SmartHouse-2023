package team14.back.service.log;

import team14.back.dto.LogDTO;

import java.util.List;

public interface LogService {

    List<LogDTO> getAllLogs();

    void add(LogDTO logDTO);

    void addInfo(LogDTO logDTO);

    void addErr(LogDTO logDTO);
}
