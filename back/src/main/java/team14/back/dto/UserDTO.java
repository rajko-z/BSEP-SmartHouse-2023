package team14.back.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private boolean deleted;
    private String role;
}

