package app.polirubro.product.mappers;

import app.polirubro.category.mappers.CategoryToCategoryResponse;
import app.polirubro.firebaseStorage.dto.ImageResponse;
import app.polirubro.firebaseStorage.mappers.ImageToImageResponse;
import app.polirubro.product.dto.ProductResponse;
import app.polirubro.product.entities.Product;

import java.util.Optional;
import java.util.function.Function;

public class ProductToProductResponse implements Function<Product, ProductResponse> {
    @Override
    public ProductResponse apply(Product product) {
        ImageResponse imageResponse = product.getImage() != null ?
                new ImageToImageResponse().apply(product.getImage()) : null;

        return ProductResponse.builder()
                .id(product.getId())
                .barcode(product.getBarcode())
                .name(product.getName())
                .description(product.getDescription())
                .quantityStock(product.getQuantityStock())
                .buyPrice(product.getBuyPrice())
                .salePrice(product.getSalePrice())
                .category(new CategoryToCategoryResponse().apply(product.getCategory()))
                .image(imageResponse)
                .build();
    }
}
