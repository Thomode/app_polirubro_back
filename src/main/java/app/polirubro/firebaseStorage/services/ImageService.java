package app.polirubro.firebaseStorage.services;

import app.polirubro.firebaseStorage.entities.Image;
import app.polirubro.firebaseStorage.repositories.ImageRepository;
import app.polirubro.product.entities.Product;
import app.polirubro.product.services.ProductService;
import app.polirubro.user.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    private final FirebaseStorageService firebaseStorageService;
    private final ProductService productService;
    private final UserService userService;

    private boolean isImageType(MultipartFile file) {
        return !file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png");
    }

    @Transactional
    public Image uploadImageProduct(Long productId, MultipartFile file) throws IOException {
        if (this.isImageType(file)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file type. Expected a .jpg or .png image");
        }

        Product productFound = this.productService.findById(productId);

        if(!this.userService.isMyRegister(productFound.getUser())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to uploading this resource");
        }

        String imageName = this.firebaseStorageService.uploadFile(file);

        Image image = Image.builder()
                .name(imageName)
                .url(this.firebaseStorageService.getUrl(imageName))
                .product(productFound)
                .createdAt(LocalDate.now())
                .build();

        return this.imageRepository.save(image);
    }

    @Transactional
    public void deleteImageProduct(Long imageId){
        Image imageFound = this.imageRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));

        if(imageFound.getProduct() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The image does not correspond to the residence");
        }

        if(!this.userService.isMyRegister(imageFound.getProduct().getUser())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to delete this resource");
        }

        try {
            this.firebaseStorageService.deleteFile(imageFound.getName());

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            this.imageRepository.delete(imageFound);
        }
    }

}
