package slash.financing.config;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
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
public class DataLoader implements CommandLineRunner {
        private final BudgetCategoryRepository budgetCategoryRepository;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) throws Exception {
                // Create and save the default budget categories
                createAndSaveDefaultCategories();

                // Create and save the default users
                createAndSaveDefaultUsers();
        }

        private void createAndSaveDefaultCategories() {
                List<BudgetCategory> defaultCategories = Arrays.asList(
                                BudgetCategory.builder()
                                                .name(BudgetCategory.DEFAULT_CATEGORY_FOOD)
                                                .description("Expenses related to food")
                                                .build(),
                                BudgetCategory.builder()
                                                .name(BudgetCategory.DEFAULT_CATEGORY_TRANSPORTATION)
                                                .description("Expenses related to transportation")
                                                .build(),
                                BudgetCategory.builder()
                                                .name(BudgetCategory.DEFAULT_CATEGORY_HOUSING)
                                                .description("Expenses related to housing")
                                                .build(),
                                BudgetCategory.builder()
                                                .name(BudgetCategory.DEFAULT_CATEGORY_ENTERTAINMENT)
                                                .description("Expenses related to entertainment")
                                                .build(),
                                BudgetCategory.builder()
                                                .name(BudgetCategory.DEFAULT_CATEGORY_HEALTHCARE)
                                                .description("Expenses related to healthcare")
                                                .build());

                budgetCategoryRepository.saveAll(defaultCategories);
        }

        private void createAndSaveDefaultUsers() {
                List<BudgetCategory> defaultCategories = budgetCategoryRepository.findAll();

                User user1 = new User(UUID.randomUUID(), UserRole.USER, "John",
                                "john@example.com",
                                passwordEncoder.encode("password1"), true, (defaultCategories));
                User user2 = new User(UUID.randomUUID(), UserRole.USER, "Jane",
                                "jane@example.com",
                                passwordEncoder.encode("password2"), true, (defaultCategories));
                User user3 = new User(UUID.randomUUID(), UserRole.ADMIN, "Ryan",
                                "gosling@example.com",
                                passwordEncoder.encode("password3"), true, (defaultCategories));
                User user4 = new User(UUID.randomUUID(), UserRole.USER, "Papzan",
                                "papzan@example.com",
                                passwordEncoder.encode("password4"), true, (defaultCategories));
                User user5 = new User(UUID.randomUUID(), UserRole.ADMIN, "Danik",
                                "danik@example.com",
                                passwordEncoder.encode("danik12345"), true, (defaultCategories));

                userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5));
        }
}