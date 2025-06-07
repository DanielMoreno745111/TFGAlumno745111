package backend.backend.services;

import backend.backend.dao.DaoEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService  implements DaoEmail {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void enviarEmail(String destino, String enlace){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destino);
        message.setSubject("Solicitud de Ingreso");
        message.setText("Su solicitud de ingreso ha sido aceptada para poder registrarse acceda al siguiente enlace:  " + enlace);
        mailSender.send(message);
    }
}
