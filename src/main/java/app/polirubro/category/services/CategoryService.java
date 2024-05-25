package app.polirubro.category.services;

import app.polirubro.category.dto.CategoryRequest;
import app.polirubro.category.entities.Category;
import app.polirubro.category.repositories.CategoryRepository;
import app.polirubro.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public Category save (CategoryRequest categoryRequest){
        if(this.categoryRepository.findByName(categoryRequest.getName()).isPresent()){
            throw new IllegalArgumentException("Category already registered");
        }

        Category newCategory = Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .createdAt(LocalDate.now())
                .user(this.userService.getCurrentUser())
                .build();

        return this.categoryRepository.save(newCategory);
    }

    public Category update(Long id, CategoryRequest categoryRequest){
        Category categoryFound = this.findById(id);

        if(!this.userService.isYourRegister(categoryFound.getUser())){
            throw new NoSuchElementException("The category does not belong to you");
        }

        categoryFound.setName(categoryRequest.getName());
        categoryFound.setDescription(categoryRequest.getDescription());

        return this.categoryRepository.save(categoryFound);
    }

    public Category findById(Long id){
        return this.categoryRepository.findById(id)
                .orElseThrow( () -> new NoSuchElementException("Category not found"));
    }

    public List<Category> findAll(){
        return this.categoryRepository.findAll();
    }

    public void delete(Long id){
        Category categoryFound = this.findById(id);

        if(!this.userService.isYourRegister(categoryFound.getUser())){
            throw new NoSuchElementException("The category does not belong to you");
        }

        this.categoryRepository.delete(categoryFound);
    }
}
