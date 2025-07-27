package service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import entity.Admin;
import entity.Section;
import repository.AdminRepository;
import repository.SectionRepository;

@Service
public class AdminService {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private SectionRepository sectionRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public boolean validateAdmin(String username, String password) {
        Optional<Admin> admin = adminRepository.findByUsername(username);
        return admin.isPresent() && passwordEncoder.matches(password, admin.get().getPassword());
    }
    
    public void changePassword(String username, String newPassword) {
        Optional<Admin> admin = adminRepository.findByUsername(username);
        if (admin.isPresent()) {
            Admin adminEntity = admin.get();
            adminEntity.setPassword(passwordEncoder.encode(newPassword));
            adminRepository.save(adminEntity);
        }
    }
    
    public void createDefaultAdmin() {
        // Create default admin if none exists
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            adminRepository.save(admin);
        }
        
        // Create default sections if none exist
        if (sectionRepository.count() == 0) {
            createDefaultSections();
        }
    }
    
    private void createDefaultSections() {
        String[] sectionNames = {"home","story", "about", "impact", "contact"};
        String[] defaultContents = {
            "Welcome to our amazing platform",
            "We are a company dedicated to excellence",
            "We are a company dedicated to excellence",
            "Making a difference in the world",
            "Get in touch with us today"
        };
        
        for (int i = 0; i < sectionNames.length; i++) {
            Section section = new Section();
            section.setName(sectionNames[i]);
            section.setContent(defaultContents[i]);
            sectionRepository.save(section);
        }
    }
}

