package slash.financing.service;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import slash.financing.data.BudgetCategory;
import slash.financing.data.User;
import slash.financing.dto.BudgetCategoryDto;
import slash.financing.exception.BudgetCategoryNotFoundException;
import slash.financing.repository.BudgetCategoryRepository;

@Service
@RequiredArgsConstructor
public class BudgetCategoryServiceImpl implements BudgetCategoryService {
    private final BudgetCategoryRepository budgetCategoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public BudgetCategory createPersonalBudgetCategory(BudgetCategoryDto budgetCategoryDto, User user) {
        BudgetCategory budgetCategory = modelMapper.map(budgetCategoryDto, BudgetCategory.class);
        budgetCategory.getUsers().add(user);
        return budgetCategoryRepository.save(budgetCategory);
    }

    @Override
    public List<BudgetCategory> getDefaultBudgetCategories() {
        return budgetCategoryRepository.findByIsPersonalFalse();
    }

    @Override
    public List<BudgetCategory> getPersonalBudgetCategories(UUID id) {
        return budgetCategoryRepository.findPersonalBudgetCategoriesByUserId(id);
    }

    @Override
    public BudgetCategory getBudgetCategoryById(UUID id) {
        return budgetCategoryRepository.findById(id)
                .orElseThrow(() -> new BudgetCategoryNotFoundException("Category not found"));
    }

    @Override
    public void deleteBudgetCategory(UUID id) {
        if (budgetCategoryRepository.existsById(id)) {
            budgetCategoryRepository.deleteById(id);
        } else {
            throw new BudgetCategoryNotFoundException("Category not found");
        }
    }

    @Override
    public BudgetCategory updateBudgetCategory(BudgetCategory budgetCategory,
            BudgetCategoryDto budgetCategoryUpdateDto) {
        modelMapper.map(budgetCategoryUpdateDto, budgetCategory);
        return budgetCategoryRepository.save(budgetCategory);
    }

}
