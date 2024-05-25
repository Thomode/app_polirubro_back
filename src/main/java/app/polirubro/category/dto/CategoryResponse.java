package app.polirubro.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    @Schema(example = "1")
    Long id;

    @Schema(example = "Tecnologia")
    String name;

    @Schema(example = "Ej: celulares, pc, camaras, etc")
    String description;
}
