package slash.financing.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import slash.financing.data.BudgetCategory;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BudgetCategoryRepositoryTest {
    @Autowired
    private BudgetCategoryRepository budgetCategoryRepository;

    @Test
    @Transactional
    public void testFindByIsPersonalFalse() {
        BudgetCategory personalCategory = BudgetCategory.builder()
                                 .name("shtory")
                                 .description("Lyublyu krasivye shtory")
                                 .isPersonal(true)
                                 .build();
        BudgetCategory nonPersonalCategory = BudgetCategory.builder()
                                .name("some")
                                .description("Ne Lyublyu krasivye shtory")
                                .isPersonal(false)
                                .build();
        budgetCategoryRepository.save(personalCategory);
        budgetCategoryRepository.save(nonPersonalCategory);

        List<BudgetCategory> nonPersonalCategories = budgetCategoryRepository.findByIsPersonalFalse();

        assertEquals(8, nonPersonalCategories.size());
        assertEquals("Food", nonPersonalCategories.get(0).getName());
    }

    @Test
    public void testFindPersonalBudgetCategoriesByUserId() {
        UUID userId = UUID.randomUUID();

        List<BudgetCategory> personalCategories = budgetCategoryRepository.findPersonalBudgetCategoriesByUserId(userId);
    }
}
