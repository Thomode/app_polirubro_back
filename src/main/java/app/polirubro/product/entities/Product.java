package app.polirubro.product.entities;

import app.polirubro.category.entities.Category;
import app.polirubro.user.entities.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "products")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
    @SequenceGenerator(name = "product_generator", sequenceName = "product_seq", allocationSize = 1)
    Long id;

    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false)
    String description;

    @Column(nullable = true)
    String imageUrl;

    @Column(nullable = false)
    int quantityStock;

    @Column(nullable = false)
    double buyPrice;

    @Column(nullable = false)
    double salePrice;

    @Column(nullable = false)
    LocalDate createdAt;

    @Column(nullable = true)
    LocalDate deletedAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
