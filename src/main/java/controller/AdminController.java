package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import dto.BlogPostDTO;
import dto.SectionDTO;
import dto.TestimonialDTO;
import service.AdminService;
import service.LandingPageService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private LandingPageService landingPageService;
    
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "admin";
    }

@PostMapping("/delete-section-image")
@ResponseBody
public String deleteSectionImage(@RequestParam Long id) {
    try {
        landingPageService.deleteSectionImage(id);
        return "Success";
    } catch (Exception e) {
        return "Error: " + e.getMessage();
    }
}
    @GetMapping("/blog")
    @ResponseBody
    public List<BlogPostDTO> getAllBlogPosts() {
        return landingPageService.getAllBlogPosts();
    }
    @GetMapping("/images/testimonial/{id}")
    public ResponseEntity<byte[]> getTestimonialImage(@PathVariable Long id) {
        byte[] imageData = landingPageService.getImage("testimonial", id);
        if (imageData == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // or detect dynamically
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }


    @GetMapping("/edit")
    public String editPage(Model model) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            model.addAttribute("sections", landingPageService.getAllSections());
            model.addAttribute("products", landingPageService.getAllProducts());
            model.addAttribute("testimonials", landingPageService.getAllTestimonials());
            model.addAttribute("blogPosts", landingPageService.getAllBlogPosts());
            return "adminpanel";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/testimonials")
    @ResponseBody
    public List<TestimonialDTO> getAllTestimonials(){
    	return landingPageService.getAllTestimonials();
    }
    
    @GetMapping("/images/blog/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getBlogImage(@PathVariable Long id) {
        byte[] imageData = landingPageService.getImage("blog", id);
        if (imageData == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // or detect MIME type
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }
    @PostMapping("/update-section")
    @ResponseBody
    public String updateSection(@RequestParam Long id, 
                               @RequestParam String content, 
                               
                               @RequestParam(required = false) MultipartFile image,
                               @RequestParam(required = false) String phone,
                               @RequestParam(required = false) String email,
                               @RequestParam(required = false) String address,
                               @RequestParam(required = false) String workingHours) {
        try {
            SectionDTO dto = new SectionDTO();
            dto.setId(id);
            dto.setContent(content);
            
            // Check if this is contact section and handle contact info
            if (phone != null || email != null || address != null || workingHours != null) {
                SectionDTO.ContactInfo contactInfo = new SectionDTO.ContactInfo();
                contactInfo.setPhone(phone);
                contactInfo.setEmail(email);
                contactInfo.setAddress(address);
                contactInfo.setWorkingHours(workingHours);
                dto.setContactInfo(contactInfo);
                
                System.out.println("Updating contact section with:");
                System.out.println("Email: " + email);
                System.out.println("Phone: " + phone);
                System.out.println("Address: " + address);
                System.out.println("Working Hours: " + workingHours);
            }
            
            landingPageService.updateSection(id, content, image, dto);
            return "Success";
        } catch (Exception e) {
            e.printStackTrace(); // Add this for debugging
            return "Error: " + e.getMessage();
        }
    }
    @PostMapping("/update-product")
    @ResponseBody
    public String updateProduct(@RequestParam Long id,
                               @RequestParam String name,
                               @RequestParam String description,
                               @RequestParam(required = false) MultipartFile image) {
        try {
            landingPageService.updateProduct(id, name, description, image);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    @PostMapping("/create-product")
    @ResponseBody
    public String createProduct(@RequestParam String name,
                               @RequestParam String description,
                               @RequestParam(required = false) MultipartFile image) {
        try {
            landingPageService.createProduct(name, description, image);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    @PostMapping("/delete-product")
    @ResponseBody
    public String deleteProduct(@RequestParam Long id) {
        try {
            landingPageService.deleteProduct(id);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    @PostMapping("/create-testimonial")
    @ResponseBody
    public String createTestimonial(@RequestParam int rating,
    							   @RequestParam String name,
                                   @RequestParam String message,
                                   @RequestParam String position,
                                   @RequestParam(required = false) MultipartFile image) {
        try {
            landingPageService.createTestimonial(rating,name, message, position, image);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    @PostMapping("/update-testimonial")
    @ResponseBody
    public String updateTestimonial(@RequestParam Long id,
    							   @RequestParam int rating,
                                   @RequestParam String name,
                                   @RequestParam String message,
                                   @RequestParam String position,
                                   @RequestParam(required = false) MultipartFile image) {
        try {
            landingPageService.updateTestimonial(id, rating,name, message, position, image);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/update-blog")
    @ResponseBody
    public String updateBlogPost(@RequestParam Long id,
                                @RequestParam String title,
                                @RequestParam String content,
                                @RequestParam(required = false) MultipartFile image) {
        try {
            landingPageService.updateBlogPost(id, title, content, image);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/delete-testimonial")
    @ResponseBody
    public String deleteTestimonial(@RequestParam Long id) {
        try {
            landingPageService.deleteTestimonial(id);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    @PostMapping("/create-blog")
    @ResponseBody
    public String createBlogPost(@RequestParam String title,
                                @RequestParam String content,
                                @RequestParam(required = false) MultipartFile image) {
        try {
            landingPageService.createBlogPost(title, content, image);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    @PostMapping("/delete-blog")
    @ResponseBody
    public String deleteBlogPost(@RequestParam Long id) {
        try {
            landingPageService.deleteBlogPost(id);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    @PostMapping("/change-password")
    @ResponseBody
    public String changePassword(@RequestParam String newPassword) {
        try {
            org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                adminService.changePassword(auth.getName(), newPassword);
                return "Success";
            }
            return "Error: Not authenticated";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
