package app.polirubro.category.entities;

import app.polirubro.product.entities.Product;
import app.polirubro.user.entities.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "categories")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_generator")
    @SequenceGenerator(name = "category_generator", sequenceName = "category_seq", allocationSize = 1)
    Long id;

    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    LocalDate createdAt;

    @Column(nullable = true)
    LocalDate deletedAt;

    @OneToMany(mappedBy = "category")
    List<Product> products;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
