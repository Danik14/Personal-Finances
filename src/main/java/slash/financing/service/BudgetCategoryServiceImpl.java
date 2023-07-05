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
import slash.financing.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class BudgetCategoryServiceImpl implements BudgetCategoryService {
    private final BudgetCategoryRepository budgetCategoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public BudgetCategory createPersonalBudgetCategory(BudgetCategoryDto budgetCategoryDto, User user) {
        // Since i have a many-to-many relationship i have to update an object on both
        // sides
        // so that they include each other
        BudgetCategory budgetCategory = modelMapper.map(budgetCategoryDto, BudgetCategory.class);
        budgetCategory.getUsers().add(user);
        user.getBudgetCategories().add(budgetCategory); // Update the user's budget categories
        budgetCategoryRepository.save(budgetCategory);
        userRepository.save(user); // Save the user with the updated budget categories
        return budgetCategory;
    }

    @Override
    public List<BudgetCategory> getDefaultBudgetCategories() {
        return budgetCategoryRepository.findByIsPersonalFalse();
    }

    @Override
    public boolean existsById(UUID id) {
        return budgetCategoryRepository.existsById(id);
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
