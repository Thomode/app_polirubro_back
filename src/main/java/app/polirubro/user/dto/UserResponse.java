package app.polirubro.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    @Schema(example = "john.doe")
    Long id;

    @Schema(example = "john.doe")
    String username;

    @Schema(example = "John")
    String firstname;

    @Schema(example = "Doe")
    String lastname;

    @Schema(example = "john.doe@example.com")
    String email;

    boolean emailVerified;

    boolean enabled;
}
