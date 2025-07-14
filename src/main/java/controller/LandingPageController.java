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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import dto.BlogPostDTO;
import dto.ProductDTO;
import dto.SectionDTO;
import dto.TestimonialDTO;
import service.LandingPageService;

@Controller
public class LandingPageController {
    
    @Autowired
    private LandingPageService landingPageService;
    
    @GetMapping("/")
    public String index(Model model) {
        // Add all the data needed for the landing page
        try {
            // Get sections for hero and about
            SectionDTO homeSection = landingPageService.getSectionByName("home");
            SectionDTO aboutSection = landingPageService.getSectionByName("about");
            SectionDTO impactSection = landingPageService.getSectionByName("impact");
            SectionDTO contactSection = landingPageService.getSectionByName("contact");
            model.addAttribute("contactSection", contactSection);

            
            // Get all other content
            List<ProductDTO> products = landingPageService.getAllProducts();
            List<TestimonialDTO> testimonials = landingPageService.getAllTestimonials();
            List<BlogPostDTO> blogPosts = landingPageService.getAllBlogPosts();
            
            // Add to model for Thymeleaf
            model.addAttribute("homeSection", homeSection);
            model.addAttribute("aboutSection", aboutSection);
            model.addAttribute("products", products);
            model.addAttribute("impactSection", impactSection);
            model.addAttribute("testimonials", testimonials);
            model.addAttribute("blogPosts", blogPosts);
            
            // Debug logging
            System.out.println("Home Section: " + (homeSection != null ? homeSection.getContent() : "null"));
            System.out.println("About Section: " + (aboutSection != null ? aboutSection.getContent() : "null"));
            System.out.println("Products count: " + products.size());
            System.out.println("Testimonials count: " + testimonials.size());
            System.out.println("Blog posts count: " + blogPosts.size());
            
        } catch (Exception e) {
            System.err.println("Error loading landing page data: " + e.getMessage());
            e.printStackTrace();
            
            // Add empty lists to prevent Thymeleaf errors
            model.addAttribute("homeSection", null);
            model.addAttribute("aboutSection", null);
            model.addAttribute("products", List.of());
            model.addAttribute("testimonials", List.of());
            model.addAttribute("blogPosts", List.of());
        }
        
        return "landing";
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
                        .body(image);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).build();
            }
        }
        return ResponseEntity.notFound().build();
    }

}