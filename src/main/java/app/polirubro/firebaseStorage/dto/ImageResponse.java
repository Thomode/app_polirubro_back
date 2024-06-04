package app.polirubro.firebaseStorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageResponse {
    @Schema(example = "1")
    Long id;

    @Schema(example = "https://firebasestorage.googleapis.com/v0/b/app-hotel-fb483.appspot.com/o/9177522c-2452-477e-9369-ac32d02f146f?alt=media")
    String url;
}
