package app.polirubro.category.mappers;

import app.polirubro.category.dto.CategoryResponse;
import app.polirubro.category.entities.Category;

import java.util.function.Function;


public class CategoryToCategoryResponse implements Function<Category, CategoryResponse> {
    @Override
    public CategoryResponse apply(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
