package pl.gienius.sknera.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderConfirmationEmail(String to, String subject, String content) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            helper.setText(content, true); // true indicates html
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("robertdzienio@gmail.com");
            logger.info("Sending mail to: " + to);
        } catch (MessagingException e) {
            throw new IllegalStateException("failed to send email", e);
        }
        mailSender.send(mimeMessage);
    }
}
