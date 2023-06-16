package team14.back.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import team14.back.enumerations.LogAction;
import team14.back.enumerations.LogStatus;

import java.time.LocalDateTime;

@Document("logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Log {

    private LogStatus status;

    private LogAction logAction;

    private LocalDateTime timestamp;

    private String trace;

    private String message;

    private String ipAddress;
}
