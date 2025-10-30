package com.iss.config;

import com.iss.model.Role;
import com.iss.model.User;
import com.iss.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initUsers(UserRepository users, PasswordEncoder encoder) {
        return args -> {
            if (users.findByEmail("admin@iss.local").isEmpty()) {
                User u = new User();
                u.setName("Admin");
                u.setEmail("admin@iss.local");
                u.setPassword(encoder.encode("password"));
                u.setRole(Role.ADMIN);
                users.save(u);
            }
            if (users.findByEmail("candidate@iss.local").isEmpty()) {
                User u = new User();
                u.setName("Candidate One");
                u.setEmail("candidate@iss.local");
                u.setPassword(encoder.encode("password"));
                u.setRole(Role.CANDIDATE);
                users.save(u);
            }
            if (users.findByEmail("interviewer@iss.local").isEmpty()) {
                User u = new User();
                u.setName("Interviewer One");
                u.setEmail("interviewer@iss.local");
                u.setPassword(encoder.encode("password"));
                u.setRole(Role.INTERVIEWER);
                users.save(u);
            }
        };
    }
}
