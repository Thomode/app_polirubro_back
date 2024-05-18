package app.polirubro.user.controllers.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Schema(example = "John")
    String firstname;

    @Schema(example = "Doe")
    String lastname;
}
