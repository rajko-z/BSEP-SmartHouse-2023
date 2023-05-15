package team14.back.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import team14.back.enumerations.DeviceType;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Device {

    private Long id;

    private DeviceType deviceType;

    private String messagesFilePath;

    private double readingFrequencyTime;        //period izmedju dva citanja poruka

    private String regexFilter;


}
