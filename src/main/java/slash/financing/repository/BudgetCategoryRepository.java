package slash.financing.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import slash.financing.data.BudgetCategory;

@Repository
public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory, UUID> {
    // not possible since we have many-to-many for users and categories,
    // and dont have specific user id directly in BudgetCategory
    // List<BudgetCategory> findByUserId(UUID userId);

    // List<BudgetCategory> findByUserIdAndNameContaining(UUID userId, String name);
}