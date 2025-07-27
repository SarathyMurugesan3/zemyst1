package service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import dto.BlogPostDTO;
import dto.ProductDTO;
import dto.SectionDTO;
import dto.TestimonialDTO;
import entity.BlogPost;
import entity.Product;
import entity.Section;
import entity.Testimonial;
import repository.BlogRepository;
import repository.ProductRepository;
import repository.SectionRepository;
import repository.TestimonialRepository;

@Service
public class LandingPageService {
    
    @Autowired
    private SectionRepository sectionRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private TestimonialRepository testimonialRepository;
    
    @Autowired
    private BlogRepository blogRepository;
    
    @Autowired
    private EmailService emailService;

    public void sendContactMail(String to, String subject, String body) {
        emailService.sendEmail(to, subject, body);
    }


public void deleteSectionImage(Long id) {
    Optional<Section> optionalSection = sectionRepository.findById(id);
    if (optionalSection.isPresent()) {
        Section section = optionalSection.get();
        
        // Don't allow image deletion for contact section
      
        
        // Clear the image data
        section.setImage(null);
        section.setImageName(null);
        section.setUpdatedAt(LocalDateTime.now());
        
        sectionRepository.save(section);
    } else {
        throw new RuntimeException("Section with ID " + id + " not found");
    }
}

    public List<SectionDTO> getAllSections() {
        return sectionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
   

    public SectionDTO getSectionByName(String name) {
        Optional<Section> section = sectionRepository.findByName(name);
        return section.map(s -> {
            SectionDTO dto = convertToDTO(s);
            dto.parseContentAsJson();  // 🔥 Parse the contact info from JSON if it's the contact section
            return dto;
        }).orElse(null);    }
    
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<TestimonialDTO> getAllTestimonials() {
        return testimonialRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<BlogPostDTO> getAllBlogPosts() {
        return blogRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public byte[] getImage(String type, Long id) {
        byte[] image = null;
        switch (type.toLowerCase()) {
            case "section":
                image = sectionRepository.findById(id).map(Section::getImage).orElse(null);
                break;
            case "about":
                image = sectionRepository.findById(id).map(Section::getImage).orElse(null);
                break;
            case "product":
                image = productRepository.findById(id).map(Product::getImage).orElse(null);
                break;
            case "impact":
            	image = sectionRepository.findById(id).map(Section::getImage).orElse(null);
            	break;
            case "testimonial":
                image = testimonialRepository.findById(id).map(Testimonial::getImage).orElse(null);
                break;
            case "blog":
                image = blogRepository.findById(id).map(BlogPost::getImage).orElse(null);
                break;
            default:
                System.out.println("Unknown image type: " + type);
                break;
        }

        return image;
    }
    
    // Section operations
    public void updateSection(Long id, String content, MultipartFile image, SectionDTO dto) throws IOException {
        Optional<Section> optionalSection = sectionRepository.findById(id);
        if (optionalSection.isPresent()) {
            Section section = optionalSection.get();

            if ("contact".equalsIgnoreCase(section.getName()) && dto.getContactInfo() != null) {
                // Store contact info in individual fields AND as JSON for backward compatibility
                SectionDTO.ContactInfo contactInfo = dto.getContactInfo();
                
                // Set individual fields in the entity
                if (dto.getAddress() != null) section.setAddress(dto.getAddress());
                if (dto.getPhone() != null) section.setPhone(dto.getPhone());
                if (dto.getEmail() != null) section.setEmail(dto.getEmail());
                if (dto.getWorkingHours() != null) section.setWorkingHours(dto.getWorkingHours());
                
                // Also store as JSON in content field for consistency
                ObjectMapper mapper = new ObjectMapper();
                String contactJson = mapper.writeValueAsString(contactInfo);
                section.setContent(contactJson);
            } else {
                section.setContent(content);
            }

            // Handle image upload (but skip for contact section)
            if (image != null && !image.isEmpty()) {
                section.setImage(image.getBytes());
                section.setImageName(image.getOriginalFilename());
            }

            section.setUpdatedAt(LocalDateTime.now());
            sectionRepository.save(section);
        } else {
            throw new RuntimeException("Section with ID " + id + " not found");
        }
    }

    // Updated convertToDTO method to handle contact info properly
    private SectionDTO convertToDTO(Section section) {
        SectionDTO dto = new SectionDTO();
        dto.setId(section.getId());
        dto.setName(section.getName());
        dto.setContent(section.getContent());
        dto.setImageName(section.getImageName());
        dto.setHasImage(section.getImage() != null && section.getImage().length > 0);
        
        if ("contact".equalsIgnoreCase(section.getName())) {
            // First try to get from individual fields (preferred)
        	dto.setAddress(section.getAddress());
            dto.setPhone(section.getPhone());
            dto.setEmail(section.getEmail());
            dto.setWorkingHours(section.getWorkingHours());
            if (section.getEmail() != null || section.getPhone() != null || 
                section.getAddress() != null || section.getWorkingHours() != null) {
                
                SectionDTO.ContactInfo info = new SectionDTO.ContactInfo();
                info.setEmail(section.getEmail());
                info.setPhone(section.getPhone());
                info.setAddress(section.getAddress());
                info.setWorkingHours(section.getWorkingHours());
                dto.setContactInfo(info);
            } else {
                // Fallback to JSON parsing from content field
                try {
                    String json = section.getContent();
                    if (json != null && !json.isBlank()) {
                        ObjectMapper mapper = new ObjectMapper();
                        SectionDTO.ContactInfo info = mapper.readValue(json, SectionDTO.ContactInfo.class);
                        dto.setContactInfo(info);
                        dto.setEmail(info.getEmail());
                        dto.setPhone(info.getPhone());
                        dto.setAddress(info.getAddress());
                        dto.setWorkingHours(info.getWorkingHours());
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Log error but don't block
                }
            }
        }

        return dto;
    }
    
    // Product operations
    public void updateProduct(Long id, String name, String description, MultipartFile image) throws IOException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(name);
            product.setDescription(description);
            if (image != null && !image.isEmpty()) {
                product.setImage(image.getBytes());  // Save image bytes
                product.setImageName(image.getOriginalFilename());
            }
            productRepository.save(product);
        }
    }

    public void createProduct(String name, String description, MultipartFile image) throws IOException {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        if (image != null && !image.isEmpty()) {
            product.setImage(image.getBytes());  // Save image bytes
            product.setImageName(image.getOriginalFilename());
        }
        productRepository.save(product);
    }

    
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    // Testimonial operations
    public void createTestimonial(int rating,String name, String message, String position, MultipartFile image) throws IOException {
        Testimonial testimonial = new Testimonial();
        testimonial.setRating(rating);
        testimonial.setName(name);
        testimonial.setMessage(message);
        testimonial.setPosition(position);
        if (image != null && !image.isEmpty()) {
            testimonial.setImage(image.getBytes());
            testimonial.setImageName(image.getOriginalFilename());
        }
        testimonialRepository.save(testimonial);
    }
    
    public void deleteTestimonial(Long id) {
        testimonialRepository.deleteById(id);
    }
    
    // Blog operations
    public void createBlogPost(String title, String content, MultipartFile image) throws IOException {
        BlogPost blogPost = new BlogPost();
        blogPost.setTitle(title);
        blogPost.setContent(content);
        if (image != null && !image.isEmpty()) {
            blogPost.setImage(image.getBytes());
            blogPost.setImageName(image.getOriginalFilename());
        }
        blogRepository.save(blogPost);
    }
    
    public void deleteBlogPost(Long id) {
        blogRepository.deleteById(id);
    }
 // Edit Testimonial
    public void updateTestimonial(Long id, int rating,String name, String message, String position, MultipartFile image) throws IOException {
        Optional<Testimonial> optionalTestimonial = testimonialRepository.findById(id);
        if (optionalTestimonial.isPresent()) {
            Testimonial testimonial = optionalTestimonial.get();
            testimonial.setName(name);
            testimonial.setMessage(message);
            testimonial.setPosition(position);
            testimonial.setRating(rating);
            if (image != null && !image.isEmpty()) {
                testimonial.setImage(image.getBytes());
                testimonial.setImageName(image.getOriginalFilename());
            }
            testimonialRepository.save(testimonial);
        }
    }

    // Edit BlogPost
    public void updateBlogPost(Long id, String title, String content, MultipartFile image) throws IOException {
        Optional<BlogPost> optionalBlogPost = blogRepository.findById(id);
        if (optionalBlogPost.isPresent()) {
            BlogPost blogPost = optionalBlogPost.get();
            blogPost.setTitle(title);
            blogPost.setContent(content);
            if (image != null && !image.isEmpty()) {
                blogPost.setImage(image.getBytes());
                blogPost.setImageName(image.getOriginalFilename());
            }
            blogRepository.save(blogPost);
        }
    }

    
    
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setImageName(product.getImageName());
        dto.setHasImage(product.getImage() != null && product.getImage().length > 0);  // Check length > 0
        return dto;
    }

    
    private TestimonialDTO convertToDTO(Testimonial testimonial) {
        TestimonialDTO dto = new TestimonialDTO();
        dto.setId(testimonial.getId());
        dto.setRating(testimonial.getRating());
        dto.setName(testimonial.getName());
        dto.setMessage(testimonial.getMessage());
        dto.setPosition(testimonial.getPosition());
        dto.setImageName(testimonial.getImageName());
        dto.setHasImage(testimonial.getImage() != null);
        return dto;
    }
    public void updateSection(SectionDTO dto) {
        Optional<Section> optionalSection = sectionRepository.findById(dto.getId());
        if (optionalSection.isPresent()) {
            Section section = optionalSection.get();
            section.setContent(dto.getContent());
            section.setUpdatedAt(LocalDateTime.now());
            
            // Optional: update contact info if this is the contact section
            if ("contact".equalsIgnoreCase(section.getName()) && dto.getContactInfo() != null) {
                // Store contact info in some way if needed
                // You can update section.setContent(), setEmail(), etc.
            }

            sectionRepository.save(section);
        } else {
            throw new RuntimeException("Section with ID " + dto.getId() + " not found");
        }
    }

    private BlogPostDTO convertToDTO(BlogPost blogPost) {
        BlogPostDTO dto = new BlogPostDTO();
        dto.setId(blogPost.getId());
        dto.setTitle(blogPost.getTitle());
        dto.setContent(blogPost.getContent());
        dto.setImageName(blogPost.getImageName());
        dto.setHasImage(blogPost.getImage() != null);
        return dto;
    }
}
