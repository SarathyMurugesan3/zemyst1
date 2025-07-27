package config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import entity.BlogPost;
import entity.Product;
import entity.Section;
import entity.Testimonial;
import repository.BlogRepository;
import repository.ProductRepository;
import repository.SectionRepository;
import repository.TestimonialRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestimonialRepository testimonialRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public void run(String... args) throws Exception {

        // Initialize sections individually if they don't exist
        createSectionIfMissing("home", "Welcome to Our Amazing Platform");
        createSectionIfMissing("about", "We are a leading company dedicated to providing exceptional products and services to our customers. With years of experience and a commitment to excellence, we deliver solutions that exceed expectations.");
        createSectionIfMissing("impact", "Our products have made a tangible difference in the lives of our customers.");
        createSectionIfMissing("story", "Our products have made a tangible difference in the lives of our customers.");        
        // Initialize contact section with proper contact information
        createContactSectionIfMissing();

        // Initialize products individually if they don't exist
        createProductIfMissing("Premium Service", "Our flagship service offering comprehensive solutions for your business needs.");
        createProductIfMissing("Consulting Package", "Expert consultation to help you make informed decisions and optimize your operations.");
        createProductIfMissing("Support Plan", "24/7 support and maintenance to ensure your systems run smoothly.");

        // Initialize testimonials individually if they don't exist
        createTestimonialIfMissing("John Smith", "Excellent service! The team exceeded our expectations and delivered outstanding results.", "CEO, Tech Solutions");
        createTestimonialIfMissing("Sarah Johnson", "Professional, reliable, and innovative. Highly recommend their services.", "Marketing Director, StartupCorp");

        // Initialize blog posts individually if they don't exist
        createBlogPostIfMissing("5 Tips for Better Business Growth", "Full content here...");
        createBlogPostIfMissing("The Future of Digital Transformation", "Full content here...");
        createBlogPostIfMissing("Customer Success Stories", "Full content here...");
    }

    private void createSectionIfMissing(String name, String content) {
        if (!sectionRepository.existsByName(name)) {
            Section section = new Section();
            section.setName(name);
            section.setContent(content);
            sectionRepository.save(section);
        }
    }

    private void createContactSectionIfMissing() {
        Optional<Section> existingContactOpt = sectionRepository.findByName("contact");

        if (existingContactOpt.isEmpty()) {
            // If not present, create it
            Section contactSection = new Section();
            contactSection.setName("contact");
            contactSection.setContent("Get in touch with us! We're here to help.");
            contactSection.setAddress("123 Innovation Drive, Tech City, TC 12345");
            contactSection.setPhone("+1 (555) 123-4567");
            contactSection.setEmail("hello@zemyst.com");
            contactSection.setWorkingHours("Mon - Fri: 9:00 AM - 6:00 PM");
            sectionRepository.save(contactSection);
            return;
        }

        Section existingContact = existingContactOpt.get(); // ✅ Unwrap Optional

        boolean needsUpdate = false;

        if (existingContact.getAddress() == null || existingContact.getAddress().isEmpty()) {
            existingContact.setAddress("123 Innovation Drive, Tech City, TC 12345");
            needsUpdate = true;
        }
        if (existingContact.getPhone() == null || existingContact.getPhone().isEmpty()) {
            existingContact.setPhone("+1 (555) 123-4567");
            needsUpdate = true;
        }
        if (existingContact.getEmail() == null || existingContact.getEmail().isEmpty()) {
            existingContact.setEmail("hello@zemyst.com");
            needsUpdate = true;
        }
        if (existingContact.getWorkingHours() == null || existingContact.getWorkingHours().isEmpty()) {
            existingContact.setWorkingHours("Mon - Fri: 9:00 AM - 6:00 PM");
            needsUpdate = true;
        }

        if (needsUpdate) {
            sectionRepository.save(existingContact);
        }
    }


    private void createProductIfMissing(String name, String description) {
        if (!productRepository.existsByName(name)) {
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            productRepository.save(product);
        }
    }

    private void createTestimonialIfMissing(String name, String message, String position) {
        if (!testimonialRepository.existsByName(name)) {
            Testimonial testimonial = new Testimonial();
            testimonial.setName(name);
            testimonial.setMessage(message);
            testimonial.setPosition(position);
            testimonialRepository.save(testimonial);
        }
    }

    private void createBlogPostIfMissing(String title, String content) {
        if (!blogRepository.existsByTitle(title)) {
            BlogPost post = new BlogPost();
            post.setTitle(title);
            post.setContent(content);
            blogRepository.save(post);
        }
    }
}