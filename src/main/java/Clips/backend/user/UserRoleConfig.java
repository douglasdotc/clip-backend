package Clips.backend.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserRoleConfig {
    @Bean
    CommandLineRunner userRoleCommandLineRunner(UserRoleRepository userRoleRepository) {
        return args -> {
            UserRole roleUser = new UserRole(1, EUserRole.USER);
            UserRole roleAdmin = new UserRole(2, EUserRole.ADMIN);

            userRoleRepository.saveAll(List.of(roleUser, roleAdmin));
        };
    }
}
