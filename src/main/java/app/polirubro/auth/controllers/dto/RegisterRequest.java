package app.polirubro.auth.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data @AllArgsConstructor @NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @Schema(example = "john.doe")
    String username;

    @Schema(example = "1234")
    String password;

    @Schema(example = "John")
    String firstname;

    @Schema(example = "Doe")
    String lastname;

    @Schema(example = "john.doe@example.com")
    String email;
}
