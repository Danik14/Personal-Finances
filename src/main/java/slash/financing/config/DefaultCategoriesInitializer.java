package slash.financing.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import slash.financing.data.BudgetCategory;
import slash.financing.repository.BudgetCategoryRepository;

@Component
public class DefaultCategoriesInitializer implements CommandLineRunner {

        private final BudgetCategoryRepository budgetCategoryRepository;

        public DefaultCategoriesInitializer(BudgetCategoryRepository budgetCategoryRepository) {
                this.budgetCategoryRepository = budgetCategoryRepository;
        }

        @Override
        public void run(String... args) {
                // Check if default categories already exist
                if (budgetCategoryRepository.count() == 0) {
                        // Create and save the default categories
                        createAndSaveDefaultCategories();
                }
        }

        @Transactional
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

                // Fetch the associated users along with the categories
                defaultCategories.forEach(category -> category.getUsers().size());

                // Save the default categories
                budgetCategoryRepository.saveAll(defaultCategories);
        }
}