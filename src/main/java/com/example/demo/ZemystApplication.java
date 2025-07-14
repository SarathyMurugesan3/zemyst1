package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import service.AdminService;


@SpringBootApplication
@ComponentScan(basePackages = {"com.example.demo", "controller", "service", "config","repository"})
@EntityScan(basePackages = "entity")
@EnableJpaRepositories(basePackages = "repository")
public class ZemystApplication implements CommandLineRunner {

	@Autowired
    private AdminService adminService;
    
    public static void main(String[] args) {
        SpringApplication.run(ZemystApplication.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
        adminService.createDefaultAdmin();
    }
}
