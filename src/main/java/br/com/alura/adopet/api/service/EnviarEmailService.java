package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.model.Adocao;
import jakarta.mail.Transport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EnviarEmailService {

    @Autowired
    private static JavaMailSender emailSender;
    public  void enviarEmail(String to,String subject, String text){
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("adopet@email.com.br");
        email.setTo(to);
        email.setSubject(subject);
        email.setText(text);
        emailSender.send(email);
    }
}
