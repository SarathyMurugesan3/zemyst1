package service;

import dto.SectionDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import service.EmailService;
import service.LandingPageService;

import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private LandingPageService landingPageService;

    @Override
    public void sendContactMail(String to, String subject, String body) {
        SectionDTO contactSection = landingPageService.getSectionByName("contact");

        if (contactSection == null || contactSection.getContactInfo() == null) {
            throw new RuntimeException("Contact section or email info missing");
        }

        String senderEmail = contactSection.getContactInfo().getEmail();
        String appPassword = contactSection.getContent(); // App password stored in content field

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(senderEmail);
        mailSender.setPassword(appPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");

            helper.setTo(to);
            helper.setFrom(senderEmail);
            helper.setSubject(subject);
            helper.setText(body, false);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Email sending failed: " + e.getMessage());
        }
    }
}
