package app.polirubro.email.entities;

import app.polirubro.user.entities.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name = "password_reset_token")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_reset_token_generator")
    @SequenceGenerator(name = "password_reset_token_generator",
            sequenceName = "password_reset_token_seq", allocationSize = 1)
    Long id;

    @Column(nullable = false)
    String token;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    User user;

    @Column(nullable = false)
    Date expiryDate;
}

