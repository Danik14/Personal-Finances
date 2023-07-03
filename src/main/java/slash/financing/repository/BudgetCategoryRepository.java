package slash.financing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import slash.financing.data.BudgetCategory;

@Repository
public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory, Long> {
    List<BudgetCategory> findByUserId(Long userId);

    // List<BudgetCategory> findByUserIdAndNameContaining(Long userId, String name);
}