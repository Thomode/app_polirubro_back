package app.polirubro.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    @Schema(example = "123432435")
    String barcode;

    @Schema(example = "Celular Samsung M22")
    @NotBlank
    String name;

    @Schema(example = "4GB RAM, 64 GB ROM y Android 12")
    @NotBlank
    String description;

    @Schema(example = "5")
    @NotNull
    @Positive
    int quantityStock;

    @Schema(example = "500000")
    @NotNull
    @Positive
    double buyPrice;

    @Schema(example = "700000")
    @NotNull
    @Positive
    double salePrice;

    @Schema(example = "1")
    @NotNull
    @Positive
    Long categoryId;
}
