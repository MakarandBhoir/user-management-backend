package com.tcs.usermanagement.config;

import com.tcs.usermanagement.model.User;
import com.tcs.usermanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner seedDemoUsers(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                log.info("Seeding demo users into the H2 database");

                User admin = new User();
                admin.setName("Demo Admin");
                admin.setEmail("admin@example.com");
                admin.setPassword("admin123");
                admin.setRole("ADMIN");

                User manager = new User();
                manager.setName("Casey Manager");
                manager.setEmail("casey.manager@example.com");
                manager.setPassword("manager123");
                manager.setRole("MANAGER");

                User analyst = new User();
                analyst.setName("Taylor Analyst");
                analyst.setEmail("taylor.analyst@example.com");
                analyst.setPassword("analyst123");
                analyst.setRole("USER");

                userRepository.save(admin);
                userRepository.save(manager);
                userRepository.save(analyst);

                log.info("Demo users seeded");
            }
        };
    }
}