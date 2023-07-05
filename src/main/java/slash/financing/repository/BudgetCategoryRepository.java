package slash.financing.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import slash.financing.data.BudgetCategory;

@Repository
public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory, UUID> {
    // not possible
    // since we
    // have many-to-many for
    // users and categories,
    // and dont
    // have specific
    // user id
    // directly in
    // BudgetCategory
    // List<BudgetCategory> findByUserEmail(String email);

    List<BudgetCategory> findByIsPersonalFalse();

    @Query("SELECT DISTINCT bc FROM User u JOIN u.budgetCategories bc WHERE u.id = :userId AND bc.isPersonal = true")
    List<BudgetCategory> findPersonalBudgetCategoriesByUserId(UUID userId);

    // List<BudgetCategory> findByUserIdAndNameContaining(UUID userId, String name);
}