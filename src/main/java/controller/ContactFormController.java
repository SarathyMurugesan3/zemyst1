package controller;

import dto.ContactFormDTO;
import dto.SectionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.LandingPageService;

@RestController
@RequestMapping("/api/contact")
public class ContactFormController {

    @Autowired
    private LandingPageService landingPageService;

    @PostMapping
    public ResponseEntity<?> handleContactForm(@RequestBody ContactFormDTO form) {
        SectionDTO contactSection = landingPageService.getSectionByName("contact");
        if (contactSection == null || contactSection.getContactInfo() == null) {
            return ResponseEntity.badRequest().body("Admin email not configured");
        }

        String adminEmail = contactSection.getContactInfo().getEmail();
        String subject = "New Contact Form: " + form.getSubject();
        String body = String.format("""
                Name: %s %s
                Email: %s
                Company: %s
                Subject: %s
                Message:
                %s
                """, form.getFirstName(), form.getLastName(), form.getEmail(),
                form.getCompany(), form.getSubject(), form.getMessage());

        try {
            landingPageService.sendContactMail(adminEmail, subject, body);
            return ResponseEntity.ok("Message sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Email sending failed: " + e.getMessage());
        }
    }
}
