package dev.madbyf.authorization.notification.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

    // public void sendEmailWithAttachment(String to, String subject, String body, String attachmentPath) {
    //     MimeMessage message = javaMailSender.createMimeMessage();
    //     MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
    //     helper.setTo(to);
    //     helper.setSubject(subject);
    //     helper.setText(body);
    //     helper.addAttachment("attachment.pdf", new File(attachmentPath));
    //     javaMailSender.send(message);
    // }

    public void sendEmailWithHtml(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        javaMailSender.send(message);
    }

}
