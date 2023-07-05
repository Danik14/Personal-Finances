package slash.financing.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import slash.financing.data.BudgetCategory;
import slash.financing.service.BudgetCategoryService;
import slash.financing.service.UserService;

@RestController
@RequestMapping("api/v1/budgetcategory")
@RequiredArgsConstructor
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

    @GetMapping("/personal")
    public ResponseEntity<?> getPersonal(Principal principal) {
        String userEmail = principal.getName();
        UUID userId = userService.getUserByEmail(userEmail).getId();

        return ResponseEntity.ok().body(budgetCategoryService.getPersonalBudgetCategories(userId));
    }

    // @PatchMapping("/{uuid}")
    // public ResponseEntity<?> updateUser(@PathVariable UUID uuid, @RequestBody
    // UserUpdateDto userUpdateDto,
    // Principal principal) {
    // String userEmail = principal.getName();
    // User user = userService.getUserById(uuid);

    // // Check if the email is being updated to a different value
    // if (!user.getEmail().equals(userEmail)) {
    // log.error(userEmail + " " + user.getEmail());

    // throw new ResponseStatusException(HttpStatus.CONFLICT,
    // "Only user who created this account can change its username!");
    // }

    // return ResponseEntity.ok().body(userService.updateUser(user, userUpdateDto));

    // }

    // @PostMapping("/personal")
    // public ResponseEntity<?> addPersonalBudgetCategory(@Valid @RequestBody
    // BudgetCategoryDto budgetCategoryDto,
    // BindingResult bindingResult,
    // UriComponentsBuilder uriBuilder, Principal principal) {
    // if (bindingResult.hasErrors()) {
    // // Handle validation errors
    // Map<String, String> errors = new HashMap<>();
    // for (FieldError error : bindingResult.getFieldErrors()) {
    // errors.put(error.getField(), error.getDefaultMessage());
    // }
    // return ResponseEntity.badRequest().body(errors);
    // }

    // String userEmail = principal.getName();
    // User user = userService.getUserByEmail(userEmail);

    // BudgetCategory budgetCategory =
    // budgetCategoryService.createPersonalBudgetCategory(budgetCategoryDto, user);

    // // Build the URI for the created resource
    // UriComponents uriComponents =
    // uriBuilder.path("/budgetcategory{uuid}").buildAndExpand(budgetCategory.getId());
    // URI createdUri = uriComponents.toUri();

    // return ResponseEntity.created(createdUri).body(budgetCategory);
    // }

    public ResponseEntity<?> deleteBudgetCategory(@PathVariable String uuid) {
        UUID id = UUID.fromString(uuid);
        if (uuid == null) {
            throw new IllegalArgumentException("Invalid UUID format");
        }

        budgetCategoryService.deleteBudgetCategory(id);

        return ResponseEntity.ok().body("BudgetCategory was successfuly deleted");
    }
}
