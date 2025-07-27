package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers(
            				"/favicon.ico","/products","/products/**","/api/contact","/send-email","/thank-you",
            			    "/", "/landing","/about", "/about/**", "/sections/home", 
            			    "/css/**", "/js/**", "/images/**", "/webjars/**",
            			    "/api/images/**", "/admin/images/**","/sections/story",
            			    "/sections/contact","/api/sections/**", "/api/products", "/api/testimonials", "/api/blog",
            			    "/contact/send", "/api/sections/update/**"
            			).permitAll()
            	        .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login") // matches your form action
                .defaultSuccessUrl("/admin/edit", true)
                .failureUrl("/admin/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}
