package app.polirubro.product.services;

import app.polirubro.category.entities.Category;
import app.polirubro.category.services.CategoryService;
import app.polirubro.product.dto.ProductRequest;
import app.polirubro.product.entities.Product;
import app.polirubro.product.repositories.ProductRepository;
import app.polirubro.user.entities.User;
import app.polirubro.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    public Product save (ProductRequest productRequest){
        if(this.productRepository.findByName(productRequest.getName()).isPresent()){
            throw new IllegalArgumentException("Product already registered");
        }

        User userSession = this.userService.getCurrentUser();
        Category categoryFound = this.categoryService.findById(productRequest.getCategoryId());

        Product newProduct = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .imageUrl("")
                .quantityStock(productRequest.getQuantityStock())
                .salePrice(productRequest.getSalePrice())
                .buyPrice(productRequest.getBuyPrice())
                .createdAt(LocalDate.now())
                .category(categoryFound)
                .user(userSession)
                .build();

        return this.productRepository.save(newProduct);
    }

    public Product update(Long id, ProductRequest productRequest){
        Product productFound = this.findById(id);

        if(!this.userService.isYourRegister(productFound.getUser())){
            throw new NoSuchElementException("The product does not belong to you");
        }

        if(this.productRepository.findByName(productRequest.getName()).isPresent()){
            throw new IllegalArgumentException("Product already registered");
        }

        productFound.setName(productRequest.getName());
        productFound.setDescription(productRequest.getDescription());
        productFound.setQuantityStock(productRequest.getQuantityStock());
        productFound.setBuyPrice(productRequest.getBuyPrice());
        productFound.setSalePrice(productRequest.getSalePrice());

        if(!productFound.getCategory().getId().equals(productRequest.getCategoryId())){
            productFound.setCategory(this.categoryService.findById(productRequest.getCategoryId()));
        }

        return  this.productRepository.save(productFound);
    }

    public Product findById(Long id){
        return this.productRepository.findById(id)
                .orElseThrow( () -> new NoSuchElementException("Product not found"));
    }

    public List<Product> findAll(){
        return this.productRepository.findAll();
    }

    public void delete(Long id){
        Product productFound = this.findById(id);

        if(!this.userService.isYourRegister(productFound.getUser())){
            throw new NoSuchElementException("The product does not belong to you");
        }

        this.productRepository.delete(productFound);
    }
}
