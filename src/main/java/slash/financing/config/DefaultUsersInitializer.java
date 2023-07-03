package slash.financing.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import slash.financing.data.BudgetCategory;
import slash.financing.data.User;
import slash.financing.enums.UserRole;
import slash.financing.repository.BudgetCategoryRepository;
import slash.financing.repository.UserRepository;

@Component
@RequiredArgsConstructor
@DependsOn("defaultCategoriesInitializer")
public class DefaultUsersInitializer implements CommandLineRunner {
        private final UserRepository userRepository;
        private final BudgetCategoryRepository budgetCategoryRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) {
                // Check if default categories already exist
                if (userRepository.count() == 0) {
                        // Create and save the default categories
                        createAndSaveDefaultCategories();
                }
        }

        private void createAndSaveDefaultCategories() {
                Set<BudgetCategory> budgetCategories = new HashSet<>(budgetCategoryRepository.findAll());

                User user1 = new User(UUID.randomUUID(), UserRole.USER, "John",
                                "john@example.com",
                                passwordEncoder.encode("password1"), true, budgetCategories);
                User user2 = new User(UUID.randomUUID(), UserRole.USER, "Jane",
                                "jane@example.com",
                                passwordEncoder.encode("password2"), true, budgetCategories);
                User user3 = new User(UUID.randomUUID(), UserRole.ADMIN, "Ryan",
                                "gosling@example.com",
                                passwordEncoder.encode("password3"), true, budgetCategories);
                User user4 = new User(UUID.randomUUID(), UserRole.USER, "Papzan",
                                "papzan@example.com",
                                passwordEncoder.encode("password4"), true, budgetCategories);
                User user5 = new User(UUID.randomUUID(), UserRole.ADMIN, "Danik",
                                "danik@example.com",
                                passwordEncoder.encode("danik12345"), true, budgetCategories);

                // repository.save(user1);
                // repository.save(user2);
                // repository.save(user3);
                // repository.save(user4);
                // repository.save(user5);

                userRepository.saveAll(List.of(user1, user2, user3, user4, user5));
        }
}
