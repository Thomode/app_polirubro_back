package app.polirubro.product.controllers;

import app.polirubro.product.dto.ProductRequest;
import app.polirubro.product.dto.ProductResponse;
import app.polirubro.product.mappers.ProductToProductResponse;
import app.polirubro.product.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<ProductResponse> save(@Validated @RequestBody ProductRequest productRequest){
        ProductResponse productResponse = new ProductToProductResponse()
                .apply(this.productService.save(productRequest));

        return ResponseEntity.ok(productResponse);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Validated @RequestBody ProductRequest productRequest){
        ProductResponse productResponse = new ProductToProductResponse()
                .apply(this.productService.update(id, productRequest));

        return ResponseEntity.ok(productResponse);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id){
        ProductResponse productResponse = new ProductToProductResponse()
                .apply(this.productService.findById(id));

        return ResponseEntity.ok(productResponse);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(){
        List<ProductResponse> productResponses = this.productService.findAll().stream()
                .map(new ProductToProductResponse()).toList();

        return ResponseEntity.ok(productResponses);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        this.productService.delete(id);

        return ResponseEntity.ok("Deleted product");
    }

}
