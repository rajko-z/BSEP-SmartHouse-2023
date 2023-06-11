package team14.back.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team14.back.enumerations.LogAction;
import team14.back.enumerations.LogStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LogDTO {
    private LogStatus status;

    private LogAction action;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime timestamp;

    private String trace;

    private String message;

    public LogDTO(LogStatus logStatus, LogAction action, String trace, String message) {
        this.status = logStatus;
        this.action = action;
        this.timestamp = LocalDateTime.now();
        this.trace = trace;
        this.message = message;
    }

    public LogDTO(LogAction action, String trace, String message) {
        this.action = action;
        this.timestamp = LocalDateTime.now();
        this.trace = trace;
        this.message = message;
    }

}
