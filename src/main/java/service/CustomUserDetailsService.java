package service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import entity.Admin;
import repository.AdminRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByUsername(username);
        if (admin.isPresent()) {
            return User.builder()
                    .username(admin.get().getUsername())
                    .password(admin.get().getPassword())
                    .authorities("ADMIN")
                    .build();
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
