package slash.financing.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import slash.financing.data.BudgetCategory;
import slash.financing.data.Expense;
import slash.financing.data.User;
import slash.financing.enums.UserRole;
import slash.financing.repository.BudgetCategoryRepository;
import slash.financing.repository.ExpenseRepository;
import slash.financing.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
        private final BudgetCategoryRepository budgetCategoryRepository;
        private final UserRepository userRepository;
        private final ExpenseRepository expenseRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) throws Exception {
                // // Create and save the default budget categories
                // createAndSaveDefaultCategories();

                // // Create and save the default users
                // createAndSaveDefaultUsers();

                // // Create and save the default expenses
                // createAndSaveDefaultExpenses();
        }

        private void createAndSaveDefaultCategories() {
                List<BudgetCategory> defaultCategories = Arrays.asList(
                                BudgetCategory.builder()
                                                .name(BudgetCategory.DEFAULT_CATEGORY_FOOD)
                                                .description("Expenses related to food")
                                                .isPersonal(false)
                                                .build(),
                                BudgetCategory.builder()
                                                .name(BudgetCategory.DEFAULT_CATEGORY_TRANSPORTATION)
                                                .description("Expenses related to transportation")
                                                .isPersonal(false)
                                                .build()
                // BudgetCategory.builder()
                // .name(BudgetCategory.DEFAULT_CATEGORY_HOUSING)
                // .description("Expenses related to housing")
                // .isPersonal(false)
                // .build(),
                // BudgetCategory.builder()
                // .name(BudgetCategory.DEFAULT_CATEGORY_ENTERTAINMENT)
                // .description("Expenses related to entertainment")
                // .isPersonal(false)
                // .build(),
                // BudgetCategory.builder()
                // .name(BudgetCategory.DEFAULT_CATEGORY_HEALTHCARE)
                // .description("Expenses related to healthcare")
                // .isPersonal(false)
                // .build()
                );

                budgetCategoryRepository.saveAll(defaultCategories);
        }

        private void createAndSaveDefaultUsers() {
                List<BudgetCategory> defaultCategories = budgetCategoryRepository.findAll();
                List<BudgetCategory> personalCategories1 = new ArrayList<>(defaultCategories);
                List<BudgetCategory> personalCategories2 = new ArrayList<>(defaultCategories);
                List<BudgetCategory> personalCategories3 = new ArrayList<>(defaultCategories);
                List<BudgetCategory> personalCategories4 = new ArrayList<>(defaultCategories);
                List<BudgetCategory> personalCategories5 = new ArrayList<>(defaultCategories);

                personalCategories1.add(BudgetCategory.builder()
                                .name("Movies")
                                .description("Expenses related to movies")
                                .isPersonal(true)
                                .build());

                personalCategories2.add(BudgetCategory.builder()
                                .name("shtory")
                                .description("Lyublyu krasivye shtory")
                                .isPersonal(true)
                                .build());

                personalCategories3.add(BudgetCategory.builder()
                                .name("Barbie - Oppenheimer 21.07")
                                .description("Barbie - Oppenheimer 21.07")
                                .isPersonal(true)
                                .build());

                personalCategories4.add(BudgetCategory.builder()
                                .name("aboniment 1fit")
                                .description("Expenses related to aboniment 1fit")
                                .isPersonal(true)
                                .build());

                personalCategories5.add(BudgetCategory.builder()
                                .name("Kaspi magaz")
                                .description("Expenses related to kaspi magaz")
                                .isPersonal(true)
                                .build());

                User user1 = new User(UUID.randomUUID(), UserRole.USER, "John",
                                "john@example.com",
                                passwordEncoder.encode("password1"), true, (personalCategories1), new ArrayList<>());
                // User user2 = new User(UUID.randomUUID(), UserRole.USER, "Jane",
                // "jane@example.com",
                // passwordEncoder.encode("password2"), true, (personalCategories2), new
                // ArrayList<>());
                User user3 = new User(UUID.randomUUID(), UserRole.ADMIN, "Ryan",
                                "gosling@example.com",
                                passwordEncoder.encode("password3"), true, (personalCategories3), new ArrayList<>());
                User user4 = new User(UUID.randomUUID(), UserRole.USER, "Papzan",
                                "papzan@example.com",
                                passwordEncoder.encode("password4"), true, (personalCategories4), new ArrayList<>());
                // User user5 = new User(UUID.randomUUID(), UserRole.ADMIN, "Danik",
                // "danik@example.com",
                // passwordEncoder.encode("danik12345"), true, (personalCategories5), new
                // ArrayList<>());

                userRepository.saveAll(Arrays.asList(user1, user3, user4));
        }

        private void createAndSaveDefaultExpenses() {
                List<BudgetCategory> defaultCategories = budgetCategoryRepository.findAll();
                User ryanGosling = userRepository.findByEmail("gosling@example.com").orElse(null);
                User papzan = userRepository.findByEmail("papzan@example.com").orElse(null);

                Expense expense1 = Expense.builder()
                                .amount(new BigDecimal(50.00))
                                .date(LocalDate.now())
                                .description("Spent a lot of money")
                                .user(ryanGosling)
                                .budgetCategory(defaultCategories.stream().findAny().orElse(null))
                                .build();

                Expense expense2 = Expense.builder()
                                .amount(new BigDecimal(50.00))
                                .date(LocalDate.now())
                                .description("Standard expense for milliarder from Vinnica")
                                .user(papzan)
                                .budgetCategory(defaultCategories.stream().findAny().orElse(null))
                                .build();

                expenseRepository.saveAll(List.of(expense1, expense2));

        }
}