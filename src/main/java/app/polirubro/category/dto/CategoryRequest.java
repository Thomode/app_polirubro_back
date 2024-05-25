package app.polirubro.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {
    @Schema(example = "Tecnologia")
    @NotBlank
    String name;

    @Schema(example = "Ej: celulares, pc, camaras, etc")
    @NotBlank
    String description;
}
