package app.polirubro.email.services;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Resource
    private Environment env;

    public void sendVerificationEmail(String to, String token) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Verificación de correo electrónico");

        String htmlMsg = "<div style='background-color: #f2f2f2; padding: 20px; text-align: center;'>"
                + "<div style='background-color: #4f0220; color: white; padding: 10px;'>"
                + "<h3 style='font-family: \"Dancing Script Variable\",font-size: 30px, cursive;'>App Polirubro</h3>"
                + "<p style='font-size: 14px;'>Verificación de Email</p>"
                + "</div>"
                + "<p>Recibes este correo electrónico porque hemos recibido tú solicitud de cración de cuenta. Si no realizaste esta solicitud, puedes ignorar este mensaje.</p>"
                + "<p>Para verificar tu email y validar tu cuenta, haz clic en el siguiente botón:</p>"
                + "<p><a href='http://localhost:5173/emailverificationPost?token=" + token + "'><button style='background-color: #ff473f; border: none; color: white; padding: 15px 32px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; cursor: pointer;'>Verificar correo electrónico</button></a></p>"
                + "<p>También puedes copiar y pegar el siguiente enlace en tu navegador:</p>"
                + "<p>" + env.getProperty("frontend.base-url") + "emailverificationPost?token=" + token + "</p>"
                + "<p>Gracias.</p>"
                + "</div>";

        helper.setText(htmlMsg, true);

        javaMailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String token) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Restablecimiento de contraseña");

        String htmlMsg = "<div style='background-color: #f2f2f2; padding: 20px; text-align: center;'>"
                + "<div style='background-color: #4f0220; color: white; padding: 10px;'>"
                + "<h3 style='font-family: \"Dancing Script Variable\",font-size: 30px, cursive;'>App Polirubro</h3>"
                + "<p style='font-size: 14px;'>Restablecimiento de Contraseña</p>"
                + "</div>"
                + "<p>Recibes este correo electrónico porque hemos recibido una solicitud para restablecer la contraseña de tu cuenta. Si no realizaste esta solicitud, puedes ignorar este mensaje.</p>"
                + "<p>Para restablecer tu contraseña, haz clic en el siguiente botón:</p>"
                + "<p><a href='http://localhost:5173/resetpassword?token=" + token + "'><button style='background-color: #ff473f; border: none; color: white; padding: 15px 32px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; cursor: pointer;'>Restablecer contraseña</button></a></p>"
                + "<p>También puedes copiar y pegar el siguiente enlace en tu navegador:</p>"
                + "<p>" + env.getProperty("frontend.base-url") + "resetpassword?token=" + token + "</p>"
                + "<p>Gracias.</p>"
                + "</div>";

        helper.setText(htmlMsg, true);

        javaMailSender.send(message);
    }
}
