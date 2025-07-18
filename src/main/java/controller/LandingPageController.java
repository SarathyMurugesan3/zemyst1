package controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import dto.BlogPostDTO;
import dto.ProductDTO;
import dto.SectionDTO;
import dto.TestimonialDTO;
import service.EmailService;
import service.LandingPageService;

@Controller
public class LandingPageController {
    
    @Autowired
    private LandingPageService landingPageService;
    @Autowired
    private EmailService emailService; 
    
    @ModelAttribute("contactSection")
    public SectionDTO injectContactSection() {
        return landingPageService.getSectionByName("contact");
    }
    
    @GetMapping("/")
    public String index(Model model) {
        // Add all the data needed for the landing page
    	try {
    		SectionDTO homeSection = landingPageService.getSectionByName("home");

            model.addAttribute("homeSection", homeSection);
            model.addAttribute("aboutSection", landingPageService.getSectionByName("about"));
            model.addAttribute("impactSection", landingPageService.getSectionByName("impact"));
            model.addAttribute("contactSection", landingPageService.getSectionByName("contact"));
            model.addAttribute("products", landingPageService.getAllProducts());
            model.addAttribute("testimonials", landingPageService.getAllTestimonials());
            model.addAttribute("blogPosts", landingPageService.getAllBlogPosts());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("homeSection", null);
            model.addAttribute("aboutSection", null);
            model.addAttribute("products", List.of());
            model.addAttribute("testimonials", List.of());
            model.addAttribute("blogPosts", List.of());
        }
        return "landing";
    }
    @PostMapping("/api/sections/update")
    @ResponseBody
    public ResponseEntity<?> updateSection(@RequestBody SectionDTO dto) {
        try {
            landingPageService.updateSection(dto); // You must have this in your service layer
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.singletonMap("error", "Update failed"));
        }
    }

    @PostMapping("/contact/send")
    @ResponseBody
    public ResponseEntity<?> handleContactForm(@RequestBody Map<String, String> payload) {
        String firstName = payload.get("firstName");
        String lastName = payload.get("lastName");
        String email = payload.get("email");
        String company = payload.get("company");
        String subject = payload.get("subject");
        String message = payload.get("message");

        String fullMessage = "From: " + firstName + " " + lastName + "\n"
                           + "Email: " + email + "\n"
                           + "Company: " + company + "\n"
                           + "Subject: " + subject + "\n\n"
                           + message;

        SectionDTO contactSection = landingPageService.getSectionByName("contact");
        if (contactSection == null || contactSection.getContactInfo() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Admin email not configured");
        }

        String adminEmail = contactSection.getContactInfo().getEmail();
        emailService.sendContactMail(adminEmail, "New Contact Form Submission", fullMessage);

        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    @GetMapping("/api/sections")
    @ResponseBody
    public List<SectionDTO> getAllSections() {
        return landingPageService.getAllSections();
    }
    
    @GetMapping("/api/sections/{name}")
    @ResponseBody
    public SectionDTO getSectionByName(@PathVariable String name) {
        return landingPageService.getSectionByName(name);
    }
    
    @GetMapping("/api/products")
    @ResponseBody
    public List<ProductDTO> getAllProducts() {
        return landingPageService.getAllProducts();
    }
    
    @GetMapping("/api/testimonials")
    @ResponseBody
    public List<TestimonialDTO> getAllTestimonials() {
        return landingPageService.getAllTestimonials();
    }
    @GetMapping("/sections/contact")
    @ResponseBody
    public ResponseEntity<?> getContactSection() {
        SectionDTO section = landingPageService.getSectionByName("contact");
        if (section == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Collections.singletonMap("error", "Contact section not found"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", section.getId());
        response.put("content", section.getContent());
        response.put("hasImage", section.isHasImage());

        SectionDTO.ContactInfo contactInfo = section.getContactInfo();
        if (contactInfo != null) {
            Map<String, String> contactMap = new HashMap<>();
            contactMap.put("phone", contactInfo.getPhone());
            contactMap.put("email", contactInfo.getEmail());
            response.put("contactInfo", contactMap);
        } else {
            response.put("contactInfo", Collections.emptyMap());
        }

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/api/blog")
    @ResponseBody
    public List<BlogPostDTO> getAllBlogPosts() {
        return landingPageService.getAllBlogPosts();
    }
    
    @GetMapping("/api/images/{type}/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String type, @PathVariable Long id) {
        byte[] image = landingPageService.getImage(type, id);
        if (image != null && image.length > 0) {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(image)) {
                String detectedType = URLConnection.guessContentTypeFromStream(bais);
                MediaType mediaType = MediaType.IMAGE_JPEG; // default
                if (detectedType != null) {
                    mediaType = MediaType.parseMediaType(detectedType);
                }
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .contentLength(image.length)
                        .body(image);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

}