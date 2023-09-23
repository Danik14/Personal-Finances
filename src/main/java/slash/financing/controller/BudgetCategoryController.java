package slash.financing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import slash.financing.data.BudgetCategory;
import slash.financing.data.User;
import slash.financing.dto.BudgetCategoryDto;
import slash.financing.enums.UserRole;
import slash.financing.exception.BudgetCategoryNotFoundException;
import slash.financing.exception.BudgetCategoryPersonalCountException;
import slash.financing.service.BudgetCategoryService;
import slash.financing.service.UserService;

import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/budgetcategory")
@RequiredArgsConstructor
@Slf4j
public class BudgetCategoryController {
    // private final ModelMapper mapper;
    private final BudgetCategoryService budgetCategoryService;
    private final UserService userService;

    @GetMapping("/defaults")
    public ResponseEntity<List<BudgetCategory>> getDefaults() {
        return ResponseEntity.ok().body(budgetCategoryService.getDefaultBudgetCategories());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<BudgetCategory> getById(@PathVariable String uuid) {
        UUID id = UUID.fromString(uuid);
        if (uuid == null) {
            throw new IllegalArgumentException("Invalid UUID format");
        }
        return ResponseEntity.ok().body(budgetCategoryService.getBudgetCategoryById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> addPersonalBudgetCategory(@Valid @RequestBody BudgetCategoryDto budgetCategoryDto,
            BindingResult bindingResult,
            UriComponentsBuilder uriBuilder, Principal principal) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        String userEmail = principal.getName();
        User user = userService.getUserByEmail(userEmail);

        long personalCategoriesCount = user.getBudgetCategories().stream()
                .filter(bugdgetCategory -> bugdgetCategory.isPersonal() == true)
                .count();

        if (personalCategoriesCount >= 10) {
            throw new BudgetCategoryPersonalCountException("You can't create more than 10 personal budget categories");
        }

        BudgetCategory budgetCategory = budgetCategoryService.createPersonalBudgetCategory(budgetCategoryDto, user);

        // Build the URI for the created resource
        UriComponents uriComponents = uriBuilder.path("/budgetcategory/{uuid}").buildAndExpand(budgetCategory.getId());
        URI createdUri = uriComponents.toUri();

        log.info("********");

        return ResponseEntity.created(createdUri).body(budgetCategory);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteBudgetCategory(@PathVariable String uuid, Principal principal) {
        UUID id = UUID.fromString(uuid);
        if (uuid == null) {
            throw new IllegalArgumentException("Invalid UUID format");
        }

        String userEmail = principal.getName();
        User user = userService.getUserByEmail(userEmail);

        BudgetCategory budgetCategory = budgetCategoryService.getBudgetCategoryById(id);

        if (!budgetCategoryService.existsById(id)) {
            throw new BudgetCategoryNotFoundException("Budget Category Not Found!");
        }

        if (user.getBudgetCategories().contains(budgetCategory) || user.getRole() == UserRole.ADMIN) {
            // Remove the budget category from the user's budgetCategories list
            user.getBudgetCategories().remove(budgetCategory);
            userService.saveUser(user); // Save the user entity after modifying the
                                        // budgetCategories list
            budgetCategoryService.deleteBudgetCategory(id);
            return ResponseEntity.ok().body("BudgetCategory was successfuly deleted");
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Only user who created this category can delete it!");
        }

    }
}
