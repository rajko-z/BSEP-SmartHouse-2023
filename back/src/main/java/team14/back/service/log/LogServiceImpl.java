package team14.back.service.log;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import team14.back.dto.alarms.AlarmDTO;
import team14.back.dto.LogDTO;
import team14.back.enumerations.LogAction;
import team14.back.enumerations.LogStatus;
import team14.back.model.Log;
import team14.back.repository.LogRepository;
import team14.back.utils.MaliciousIpAddresses;

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
                .map(l -> new LogDTO(l.getStatus(), l.getLogAction(), l.getTimestamp(),l.getTrace(),l.getMessage(), l.getIpAddress()))
                .sorted((l1, l2) -> l2.getTimestamp().compareTo(l1.getTimestamp()))
                .collect(Collectors.toList());
    }

    @Override
    public void add(LogDTO logDTO) {
        checkIfIpAddressIsMalicious(logDTO);
        logRepository.save(new Log(logDTO.getStatus(), logDTO.getAction(), logDTO.getTimestamp(),
                logDTO.getMessage(), logDTO.getMessage(), logDTO.getIpAddress()));
        simpMessagingTemplate.convertAndSend("/new-log", logDTO);
    }

    @Override
    public void addInfo(LogDTO logDTO) {
        logDTO.setStatus(LogStatus.INFO);
        checkIfIpAddressIsMalicious(logDTO);
        logRepository.save(new Log(logDTO.getStatus(), logDTO.getAction(), logDTO.getTimestamp(),
                logDTO.getTrace(), logDTO.getMessage(), logDTO.getIpAddress()));
        simpMessagingTemplate.convertAndSend("/new-log", logDTO);
    }

    @Override
    public void addErr(LogDTO logDTO) {
        checkIfIpAddressIsMalicious(logDTO);
        logDTO.setStatus(LogStatus.ERROR);
        logRepository.save(new Log(logDTO.getStatus(), logDTO.getAction(), logDTO.getTimestamp(),
                logDTO.getTrace(), logDTO.getMessage(), logDTO.getIpAddress()));
        simpMessagingTemplate.convertAndSend("/new-log", logDTO);
        simpMessagingTemplate.convertAndSend("/alarm", new AlarmDTO(logDTO.getStatus(), logDTO.getAction(), logDTO.getMessage()));
    }

    private void checkIfIpAddressIsMalicious(LogDTO logDTO) {
        List<String> maliciousAddresses = MaliciousIpAddresses.getMaliciousAddresses();
        if(maliciousAddresses.contains(logDTO.getIpAddress())){
            logDTO.setAction(LogAction.MALICIOUS_IP_ADDRESS);
            logDTO.setStatus(LogStatus.ERROR);
            simpMessagingTemplate.convertAndSend("/alarm", new AlarmDTO(logDTO.getStatus(), logDTO.getAction(), logDTO.getMessage()));
        }
    }
}
