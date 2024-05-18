package app.polirubro.user.controllers.Mappers;

import app.polirubro.user.controllers.DTO.UserResponse;
import app.polirubro.user.entities.User;

import java.util.function.Function;

public class UserToUserResponse implements Function<User, UserResponse> {
    @Override
    public UserResponse apply(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .emailVerified(user.isEmailVerified())
                .enabled(user.isEnabled())
                .build();
    }
}
