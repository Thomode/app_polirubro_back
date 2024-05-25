package app.polirubro.category.controllers;

import app.polirubro.category.dto.CategoryRequest;
import app.polirubro.category.dto.CategoryResponse;
import app.polirubro.category.mappers.CategoryToCategoryResponse;
import app.polirubro.category.services.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<CategoryResponse> save(@Validated @RequestBody CategoryRequest categoryRequest){
        CategoryResponse categoryResponse = new CategoryToCategoryResponse()
                .apply(this.categoryService.save(categoryRequest));

        return ResponseEntity.ok(categoryResponse);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long id,
            @Validated @RequestBody CategoryRequest categoryRequest
    ){
        CategoryResponse categoryResponse = new CategoryToCategoryResponse()
                .apply(this.categoryService.update(id, categoryRequest));

        return ResponseEntity.ok(categoryResponse);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id){
        CategoryResponse categoryResponse = new CategoryToCategoryResponse()
                .apply(this.categoryService.findById(id));

        return ResponseEntity.ok(categoryResponse);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findByAll(){
        List<CategoryResponse> categoryResponses = this.categoryService.findAll()
                .stream().map(new CategoryToCategoryResponse()).toList();

        return ResponseEntity.ok(categoryResponses);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        this.categoryService.delete(id);

        return ResponseEntity.ok("Deleted category");
    }
}
