package dev.madbyf.main.notification.domain.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

	
}



// private final JavaMailSender mailSender;

// public void sendEmail(
//       String to,
//       String subject,
//       String body) {

//    SimpleMailMessage message = new SimpleMailMessage();

//    message.setTo(to);
//    message.setSubject(subject);
//    message.setText(body);

//    mailSender.send(message);
// }

// public void sendHtmlEmail(
//       String to,
//       String subject,
//       String htmlContent) throws MessagingException {

//    MimeMessage message = mailSender.createMimeMessage();

//    MimeMessageHelper helper =
//             new MimeMessageHelper(message, true, "UTF-8");

//    helper.setTo(to);
//    helper.setSubject(subject);
//    helper.setText(htmlContent, true);

//    mailSender.send(message);
// }

// public void sendWithAttachment(
//         String to,
//         String subject,
//         String html,
//         File file) throws MessagingException {

//     MimeMessage message = mailSender.createMimeMessage();

//     MimeMessageHelper helper =
//             new MimeMessageHelper(message, true);

//     helper.setTo(to);
//     helper.setSubject(subject);
//     helper.setText(html, true);

//     helper.addAttachment(file.getName(), file);

//     mailSender.send(message);
// }