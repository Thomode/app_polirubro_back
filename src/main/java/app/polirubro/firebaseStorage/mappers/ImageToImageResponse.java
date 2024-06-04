package app.polirubro.firebaseStorage.mappers;

import app.polirubro.firebaseStorage.dto.ImageResponse;
import app.polirubro.firebaseStorage.entities.Image;

import java.util.function.Function;

public class ImageToImageResponse implements Function<Image, ImageResponse> {
    @Override
    public ImageResponse apply(Image image) {
        return ImageResponse.builder()
                .id(image.getId())
                .url(image.getUrl())
                .build();
    }
}
