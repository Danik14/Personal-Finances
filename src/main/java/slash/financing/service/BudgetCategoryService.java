package slash.financing.service;

import java.util.List;
import java.util.UUID;

import slash.financing.data.BudgetCategory;
import slash.financing.dto.BudgetCategoryUpdateDto;

public interface BudgetCategoryService {
    BudgetCategory createPersonalBudgetCategory(BudgetCategory budgetCategory);

    List<BudgetCategory> getAllBudgetCategories();

    BudgetCategory getBudgetCategoryById(UUID id);

    void deleteBudgetCategory(UUID id);

    BudgetCategory updateBudgetCategory(BudgetCategory budgetCategory, BudgetCategoryUpdateDto budgetCategoryUpdateDto);
}
