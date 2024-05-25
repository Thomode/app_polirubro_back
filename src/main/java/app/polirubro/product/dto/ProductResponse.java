package app.polirubro.product.dto;

import app.polirubro.category.dto.CategoryResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    @Schema(example = "1")
    Long id;

    @Schema(example = "Celular Samsung M22")
    String name;

    @Schema(example = "4GB RAM, 64 GB ROM y Android 12")
    String description;

    @Schema(example = "5")
    int quantityStock;

    @Schema(example = "500000")
    double salePrice;

    @Schema(example = "700000")
    double buyPrice;

    CategoryResponse category;
}
