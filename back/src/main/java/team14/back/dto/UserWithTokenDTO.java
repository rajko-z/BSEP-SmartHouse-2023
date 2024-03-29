package team14.back.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserWithTokenDTO {
    private UserDTO user;
    private String token;

    @JsonIgnore
    private String fingerprint;
}
