package team14.back.service.log;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import team14.back.dto.LogDTO;
import team14.back.dto.crt.VerifyCertificateResponseDTO;
import team14.back.enumerations.LogStatus;
import team14.back.model.Log;
import team14.back.repository.LogRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public List<LogDTO> getAllLogs() {
        return logRepository.findAll().stream()
                .map(l -> new LogDTO(l.getStatus(),l.getAction(),l.getTimestamp(),l.getTrace(),l.getMessage()))
                .collect(Collectors.toList());
    }

    @Override
    public void add(LogDTO logDTO) {
        logRepository.save(new Log(logDTO.getStatus(), logDTO.getAction(), logDTO.getTimestamp(), logDTO.getMessage(), logDTO.getMessage()));
        simpMessagingTemplate.convertAndSend("/new-log", logDTO);
    }

    @Override
    public void addInfo(LogDTO logDTO) {
        logDTO.setStatus(LogStatus.INFO);
        logRepository.save(new Log(LogStatus.INFO, logDTO.getAction(), logDTO.getTimestamp(), logDTO.getTrace(), logDTO.getMessage()));
        simpMessagingTemplate.convertAndSend("/new-log", logDTO);
    }

    @Override
    public void addErr(LogDTO logDTO) {
        logDTO.setStatus(LogStatus.ERROR);
        logRepository.save(new Log(LogStatus.ERROR, logDTO.getAction(), logDTO.getTimestamp(), logDTO.getTrace(), logDTO.getMessage()));
        simpMessagingTemplate.convertAndSend("/new-log", logDTO);
    }
}
