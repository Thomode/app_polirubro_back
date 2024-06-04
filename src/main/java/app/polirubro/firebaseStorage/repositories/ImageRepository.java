package app.polirubro.firebaseStorage.repositories;

import app.polirubro.firebaseStorage.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
