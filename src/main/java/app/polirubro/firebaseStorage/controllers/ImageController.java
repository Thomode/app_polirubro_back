package app.polirubro.firebaseStorage.controllers;

import app.polirubro.firebaseStorage.dto.ImageResponse;
import app.polirubro.firebaseStorage.mappers.ImageToImageResponse;
import app.polirubro.firebaseStorage.services.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/images")
@Tag(name = "Images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/product/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<ImageResponse> uploadImageProduct (
            @PathVariable Long id,
            @RequestParam(value = "file") MultipartFile file
    ) throws IOException {
        ImageResponse imageResponse = new ImageToImageResponse()
                .apply(this.imageService.uploadImageProduct(id, file));

        return ResponseEntity.ok(imageResponse);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{id}/product")
    public ResponseEntity<String> deleteImageProduct(@PathVariable Long id){
        this.imageService.deleteImageProduct(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
