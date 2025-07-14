package config;

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
        
        // Initialize sections if they don't exist
        if (sectionRepository.count() == 0) {
            Section heroSection = new Section();
            heroSection.setName("hero");
            heroSection.setContent("Welcome to Our Amazing Platform");
            sectionRepository.save(heroSection);
            
            Section aboutSection = new Section();
            aboutSection.setName("about");
            aboutSection.setContent("We are a leading company dedicated to providing exceptional products and services to our customers. With years of experience and a commitment to excellence, we deliver solutions that exceed expectations.");
            sectionRepository.save(aboutSection);
        }
        
        // Initialize products if they don't exist
        if (productRepository.count() == 0) {
            Product product1 = new Product();
            product1.setName("Premium Service");
            product1.setDescription("Our flagship service offering comprehensive solutions for your business needs.");
            productRepository.save(product1);
            
            Product product2 = new Product();
            product2.setName("Consulting Package");
            product2.setDescription("Expert consultation to help you make informed decisions and optimize your operations.");
            productRepository.save(product2);
            
            Product product3 = new Product();
            product3.setName("Support Plan");
            product3.setDescription("24/7 support and maintenance to ensure your systems run smoothly.");
            productRepository.save(product3);
        }
        
        // Initialize testimonials if they don't exist
        if (testimonialRepository.count() == 0) {
            Testimonial testimonial1 = new Testimonial();
            testimonial1.setName("John Smith");
            testimonial1.setMessage("Excellent service! The team exceeded our expectations and delivered outstanding results.");
            testimonial1.setPosition("CEO, Tech Solutions");
            testimonialRepository.save(testimonial1);
            
            Testimonial testimonial2 = new Testimonial();
            testimonial2.setName("Sarah Johnson");
            testimonial2.setMessage("Professional, reliable, and innovative. Highly recommend their services.");
            testimonial2.setPosition("Marketing Director, StartupCorp");
            testimonialRepository.save(testimonial2);
        }
        
        // Initialize blog posts if they don't exist
        if (blogRepository.count() == 0) {
            BlogPost post1 = new BlogPost();
            post1.setTitle("5 Tips for Better Business Growth");
            post1.setContent("Full content here...");
            blogRepository.save(post1);
            
            BlogPost post2 = new BlogPost();
            post2.setTitle("The Future of Digital Transformation");
            
            post2.setContent("Full content here...");
            blogRepository.save(post2);
            
            BlogPost post3 = new BlogPost();
            post3.setTitle("Customer Success Stories");
            
            post3.setContent("Full content here...");
            blogRepository.save(post3);
        }
    }
}