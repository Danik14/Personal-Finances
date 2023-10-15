package slash.financing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import slash.financing.data.BudgetCategory;

import java.util.List;
import java.util.UUID;

@Repository
public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory, UUID> {
    List<BudgetCategory> findByIsPersonalFalse();

    @Query("SELECT DISTINCT bc FROM User u JOIN u.budgetCategories bc WHERE u.id = :userId AND bc.isPersonal = true")
    List<BudgetCategory> findPersonalBudgetCategoriesByUserId(UUID userId);
}