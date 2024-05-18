package app.polirubro.email.services;

import app.polirubro.email.entities.EmailVerificationToken;
import app.polirubro.email.repositories.EmailVerificationTokenRepository;
import app.polirubro.user.entities.User;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationTokenService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Resource
    private Environment env;

    public EmailVerificationToken createToken(User user){
        String token = UUID.randomUUID().toString();

        EmailVerificationToken emailVerificationToken =
                EmailVerificationToken.builder()
                        .token(token)
                        .user(user)
                        .expiryDate(this.calculateDate())
                        .build();

        return this.emailVerificationTokenRepository.save(emailVerificationToken);
    }

    private Date calculateDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, env.getProperty("token.email-verification.duration", Integer.class));

        return calendar.getTime();
    }

    public EmailVerificationToken findByToken(String token){
        return this.emailVerificationTokenRepository.findByToken(token);
    }

    public void deleteToken(EmailVerificationToken emailVerificationToken) {
        this.emailVerificationTokenRepository.delete(emailVerificationToken);
    }
}
