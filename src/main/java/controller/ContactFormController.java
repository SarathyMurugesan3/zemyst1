package controller;

import dto.ContactFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ContactFormController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/contact")
    public String showForm(Model model) {
        model.addAttribute("contactForm", new ContactFormDTO());
        return "contact"; // contact.html
    }

    @PostMapping("/send-email")
    public String sendEmail(@ModelAttribute("contactForm") ContactFormDTO form, Model model) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("srtupepr@gmail.com");
            message.setTo("srtupepr@gmail.com");
            message.setTo("srtupepr@gmail.com");
            message.setSubject("Contact Form: " + form.getSubject());

            String body = """
                    First Name: %s
                    Last Name: %s
                    Email: %s
                    Company: %s
                    Rating: %d

                    Message:
                    %s
                    """.formatted(
                    form.getFirstName(),
                    form.getLastName(),
                    form.getEmail(),
                    form.getCompany(),
                    form.getRating(),
                    form.getMessage()
            );

            message.setText(body);
            mailSender.send(message);

            model.addAttribute("message", "Email sent successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "Failed to send email: " + e.getMessage());
        }
        return "thank-you";
    }
}