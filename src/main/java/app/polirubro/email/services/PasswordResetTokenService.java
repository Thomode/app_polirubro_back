package app.polirubro.email.services;

import app.polirubro.email.entities.PasswordResetToken;
import app.polirubro.email.repositories.PasswordResetTokenRepository;
import app.polirubro.user.entities.User;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Resource
    private Environment env;

    public PasswordResetToken createToken(User user) {
        // Check if a token already exists for the user
        PasswordResetToken existingToken = passwordResetTokenRepository.findByUser(user);

        if (existingToken != null) {
            passwordResetTokenRepository.delete(existingToken);
        }

        // Generate a new token
        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken =
                PasswordResetToken
                    .builder()
                    .token(token)
                    .user(user)
                    .expiryDate(this.calculateExpiryDate())
                    .build();

        return this.passwordResetTokenRepository.save(passwordResetToken);
    }

    private Date calculateExpiryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, env.getProperty("token.forgot-password.duration", Integer.class));
        return calendar.getTime();
    }

    public PasswordResetToken getByToken(String token) {
        return this.passwordResetTokenRepository.findByToken(token);
    }

    public void deleteToken(PasswordResetToken token) {
        this.passwordResetTokenRepository.delete(token);
    }
}
