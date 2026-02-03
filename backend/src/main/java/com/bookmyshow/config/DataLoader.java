package com.bookmyshow.config;

import com.bookmyshow.entity.Show;
import com.bookmyshow.entity.User;
import com.bookmyshow.entity.UserRole;
import com.bookmyshow.repository.ShowRepository;
import com.bookmyshow.repository.UserRepository;
import com.bookmyshow.util.CaptchaValidator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final CaptchaValidator captchaValidator;

    public DataLoader(UserRepository userRepository, ShowRepository showRepository, CaptchaValidator captchaValidator) {
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.captchaValidator = captchaValidator;
    }

    @Override
    public void run(String... args) {
        String adminPhone = "9999999999";
        User admin = userRepository.findByPhoneNumber(adminPhone).orElse(null);
        if (admin == null) {
            admin = User.builder()
                    .phoneNumber(adminPhone)
                    .passwordHash(captchaValidator.hashPassword("Admin@123"))
                    .fullName("Admin User")
                    .email("admin@bookmyshow.local")
                    .role(UserRole.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println("[DataLoader] Admin user seeded: phone=9999999999, password=Admin@123");
        } else if (admin.getRole() != UserRole.ADMIN) {
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
            System.out.println("[DataLoader] Admin role updated for phone=9999999999");
        }

        String userPhone = "1234567890";
        if (!userRepository.existsByPhoneNumber(userPhone)) {
            User user = User.builder()
                    .phoneNumber(userPhone)
                    .passwordHash(captchaValidator.hashPassword("User@123"))
                    .fullName("Test User")
                    .email("user@bookmyshow.local")
                    .role(UserRole.USER)
                    .lastSelectedCity("Mumbai")
                    .build();
            userRepository.save(user);
            System.out.println("[DataLoader] Test user seeded: phone=1234567890, password=User@123");
        }

        // Fix any existing shows with null availableSeats
        showRepository.findAll().forEach(show -> {
            if (show.getAvailableSeats() == null) {
                show.setAvailableSeats(show.getTotalSeats() != null ? show.getTotalSeats() : 0);
                showRepository.save(show);
            }
        });
    }
}
