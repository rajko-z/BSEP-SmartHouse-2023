package team14.back.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeviceDTO {
    @NotBlank
    @Length(max=256)
    private Long id;

    @NotBlank
    @Length(max=256)
    private String deviceType;

    @NotBlank
    @Length(max=256)
    private String messagesFilePath;
}
