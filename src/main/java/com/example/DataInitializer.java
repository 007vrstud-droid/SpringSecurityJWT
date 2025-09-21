package com.example;

import com.example.models.AppUser;
import com.example.models.Role;
import com.example.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
 //      userRepository.deleteAll();

        if (userRepository.count() == 0) {
            List<AppUser> users=new ArrayList<>();
            AppUser user = new AppUser();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole(Role.USER);
            users.add(user);

            AppUser moderator = new AppUser();
            moderator.setUsername("moderator");
            moderator.setPassword(passwordEncoder.encode("moderator123"));
            moderator.setRole(Role.MODERATOR);
            users.add(moderator);

            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.SUPER_ADMIN);
            users.add(admin);

            userRepository.saveAll(users);
        }
    }
}
