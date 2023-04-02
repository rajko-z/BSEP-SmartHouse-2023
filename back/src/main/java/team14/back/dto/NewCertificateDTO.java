package team14.back.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import team14.back.enumerations.IntermediateCA;
import team14.back.enumerations.TemplateType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewCertificateDTO {
    @Valid
    private SubjectDataDTO subjectData;

    @NotNull
    private TemplateType templateType;

    @NotNull
    private IntermediateCA intermediateCA;

    @NotNull
    private KeyUsageDTO keyUsage;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime validUntil;
}
