package fit.ktpm_tuan_06_20036311_tranquanghien.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMail {
    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String toEmail, String subject, String text) {
        SimpleMailMessage message =new SimpleMailMessage();
        message.setFrom("tqhien27102002@gmail.com");
        message.setTo(toEmail);
        message.setText(text);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("send to "+toEmail+" successfully");
    }
}
