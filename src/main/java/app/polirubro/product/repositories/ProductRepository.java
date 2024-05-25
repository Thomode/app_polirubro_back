package app.polirubro.product.repositories;

import app.polirubro.product.entities.Product;
import app.polirubro.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    List<Product> findByUser(User user);
    @Query("SELECT p FROM Product p WHERE (LOWER(p.barcode) LIKE LOWER(CONCAT(:search, '%')) OR LOWER(p.name) LIKE LOWER(CONCAT(:search, '%'))) AND p.user = :user")
    List<Product> findByBarcodeOrName(
            @Param("search") String search,
            @Param("user") User user
    );
    Optional<Product> findByBarcodeAndUser(String barcode, User user);
}
