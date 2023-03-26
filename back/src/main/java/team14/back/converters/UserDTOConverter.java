package team14.back.converters;


import team14.back.dto.UserDTO;
import team14.back.dto.UserWithTokenDTO;
import team14.back.model.User;

public class UserDTOConverter {

    private UserDTOConverter() { }

    public static UserDTO convertBase(User user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .deleted(user.isDeleted())
                .role(user.getRole().getName())
                .build();
    }

    public static UserWithTokenDTO convertToUserWithToken(User user, String token) {
        return UserWithTokenDTO.builder()
                .user(convertBase(user))
                .token(token)
                .build();
    }
}
