package com.candidate.pks.config;

import com.candidate.pks.auth.model.User;
import com.candidate.pks.auth.model.UserRole;
import com.candidate.pks.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User adminUser = User.builder()
                    .username("admin")
                    .password(new User().getHashPassword("admin123"))
                    .role(UserRole.ADMIN)
                    .active(true)
                    .build();

            userRepository.save(adminUser);
            log.info("Admin user created with username: admin and password: admin123");
        } else {
            log.info("Admin user already exists.");
        }
    }
}
