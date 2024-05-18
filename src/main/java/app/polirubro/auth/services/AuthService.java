package app.polirubro.auth.services;

import app.polirubro.auth.controllers.dto.*;
import app.polirubro.auth.jwt.JwtService;
import app.polirubro.email.entities.EmailVerificationToken;
import app.polirubro.email.entities.PasswordResetToken;
import app.polirubro.email.services.EmailService;
import app.polirubro.email.services.EmailVerificationTokenService;
import app.polirubro.email.services.PasswordResetTokenService;
import app.polirubro.user.entities.User;
import app.polirubro.user.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final EmailService emailService;

    public AuthResponse login(LoginRequest request) {
        // Get user
        String usernameOrEmail = request.getUsername();
        User user = null;

        if (isEmail(usernameOrEmail)) {
            user = userRepository.findByEmail(usernameOrEmail).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found with the provided email"));
        } else {
            user = userRepository.findByUsername(usernameOrEmail).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found with the provided username"));
        }

        // Check user has verified email
        if (!user.isEmailVerified()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The email has not been verified.");
        }

        // Check user has enabled
        if (!user.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User disabled");
        }

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.getToken(userDetails);

        return AuthResponse.builder().token(token).build();
    }

    public RegisterResponse register(RegisterRequest request) throws MessagingException {
        // username cannot be a email nor be duplicated
        if (isEmail(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The username cannot be an email.");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The username is already in use.");
        }

        // Create user
        User userCreated = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .emailVerified(false)
                .enabled(true)
                .build();

        User userSaved = userRepository.save(userCreated);

        // Send verification email
        EmailVerificationToken emailVerificationToken = this.emailVerificationTokenService.createToken(userSaved);
        this.emailService.sendVerificationEmail(userSaved.getEmail(), emailVerificationToken.getToken());

        // Response
        return RegisterResponse
                .builder()
                .message("User registered. Please verify your email")
                .build();
    }

    public VerifyEmailResponse verifyEmail(String token) {
        EmailVerificationToken emailVerificationToken = this.emailVerificationTokenService.findByToken(token);

        if (emailVerificationToken == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The verification token is invalid");
        }
        if (emailVerificationToken.getExpiryDate().before(new Date())) {
            this.emailVerificationTokenService.deleteToken(emailVerificationToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "The verification token has expired.");

        }

        User user = emailVerificationToken.getUser();
        user.setEmailVerified(true);

        this.userRepository.save(user);
        this.emailVerificationTokenService.deleteToken(emailVerificationToken);

        return VerifyEmailResponse
                .builder()
                .message("Email verifier. You can now login.")
                .token(jwtService.getToken(user))
                .build();
    }

    public ForgotPasswordResponse forgotPassword(String email) throws MessagingException  {
        User user = this.userRepository.findByEmail(email).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user was found with the provided email.")
        );

        PasswordResetToken passwordResetToken = this.passwordResetTokenService.createToken(user);
        this.emailService.sendPasswordResetEmail(user.getEmail(), passwordResetToken.getToken());

        return ForgotPasswordResponse
                .builder()
                .message("An email has been sent with instructions to reset your password.")
                .build();
    }

    public ResetPasswordResponse resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = this.passwordResetTokenService.getByToken(token);

        if (passwordResetToken == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password reset token is invalid");
        }

        if (passwordResetToken.getExpiryDate().before(new Date())) {
            this.passwordResetTokenService.deleteToken(passwordResetToken);

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The password reset token has expired.");
        }

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));

        this.userRepository.save(user);
        this.passwordResetTokenService.deleteToken(passwordResetToken);

        return ResetPasswordResponse
                .builder()
                .message("The password has been reset successfully.")
                .build();
    }

    private boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
