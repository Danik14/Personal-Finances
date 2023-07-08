package slash.financing.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import slash.financing.data.BudgetCategory;
import slash.financing.data.Expense;
import slash.financing.data.User;
import slash.financing.dto.Expense.ExpenseDto;
import slash.financing.service.BudgetCategoryService;
import slash.financing.service.ExpenseService;
import slash.financing.service.UserService;

@RestController
@RequestMapping("api/v1/expenses")
@RequiredArgsConstructor
// @Slf4j
public class ExpenseController {
    private final UserService userService;
    private final ExpenseService expenseService;
    private final BudgetCategoryService budgetCategoryService;
    private final ModelMapper modelMapper;

    @GetMapping(produces = "application/json")
    public ResponseEntity<Map<String, Object>> getUserExpenses(Principal principal) {
        String userEmail = principal.getName();
        User user = userService.getUserByEmail(userEmail);

        Map<String, Object> response = new HashMap<>();

        List<Expense> expenses = expenseService.getAllUserExpenses(user);
        BigDecimal totalExpenses = expenseService.getTotalMoneySpentForUser(user);
        List<ExpenseDto> expenseResponses = expenses.stream()
                .map(expense -> modelMapper.map(expense, ExpenseDto.class))
                .collect(Collectors.toList());

        response.put("total", totalExpenses);
        response.put("expenses", expenseResponses);

        return ResponseEntity.ok(response);

    }

    @GetMapping(params = { "startDate", "endDate" })
    public ResponseEntity<Map<String, Object>> getTotalExpensesForUserInDateRange(Principal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Map<String, Object> response = new HashMap<>();

        String userEmail = principal.getName();
        User user = userService.getUserByEmail(userEmail);
        BigDecimal totalExpenses = expenseService.getTotalMoneySpentForUserInDateRange(user, startDate, endDate);
        List<Expense> expenses = expenseService.getExpensesForUserInDateRange(user, startDate, endDate);
        List<ExpenseDto> expenseResponses = expenses.stream()
                .map(expense -> modelMapper.map(expense, ExpenseDto.class))
                .collect(Collectors.toList());

        response.put("total", totalExpenses);
        response.put("expenses", expenseResponses);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/budgetCategory/{budgetCategoryId}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getUserExpensesByBudgetCategory(@PathVariable UUID budgetCategoryId,
            Principal principal) {
        String userEmail = principal.getName();
        User user = userService.getUserByEmail(userEmail);

        BudgetCategory budgetCategory = budgetCategoryService.getBudgetCategoryById(budgetCategoryId);

        if (user.getBudgetCategories().contains(budgetCategory)) {
            System.out.println("**************************");
        }

        Map<String, Object> response = new HashMap<>();

        List<Expense> expenses = expenseService.getByBudgetCategory(budgetCategory);
        BigDecimal totalExpenses = expenseService.getTotalMoneySpentForBudgetCategory(budgetCategory);

        List<ExpenseDto> expenseResponses = expenses.stream()
                .map(expense -> modelMapper.map(expense, ExpenseDto.class))
                .collect(Collectors.toList());

        response.put("total", totalExpenses);
        response.put("expenses", expenseResponses);

        return ResponseEntity.ok(response);

    }

    @GetMapping(path = "/budgetCategory/{budgetCategoryId}", produces = "application/json", params = { "startDate",
            "endDate" })
    public ResponseEntity<Map<String, Object>> getUserExpensesByBudgetCategory(@PathVariable UUID budgetCategoryId,
            Principal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        // String userEmail = principal.getName();
        // User user = userService.getUserByEmail(userEmail);

        BudgetCategory budgetCategory = budgetCategoryService.getBudgetCategoryById(budgetCategoryId);

        Map<String, Object> response = new HashMap<>();

        List<Expense> expenses = expenseService.getByBudgetCategoryAndDateBetween(budgetCategory, startDate, endDate);
        BigDecimal totalExpenses = expenseService.getTotalMoneySpentForBudgetCategoryInDateRange(budgetCategory,
                startDate, endDate);

        List<ExpenseDto> expenseResponses = expenses.stream()
                .map(expense -> modelMapper.map(expense, ExpenseDto.class))
                .collect(Collectors.toList());

        response.put("total", totalExpenses);
        response.put("expenses", expenseResponses);

        return ResponseEntity.ok(response);

    }

    @PostMapping("/{budgetCategoryId}")
    public ResponseEntity<?> addUserExpense(@PathVariable UUID budgetCategoryId,
            @Valid @RequestBody ExpenseDto expenseDto, Principal principal,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        String userEmail = principal.getName();
        User user = userService.getUserByEmail(userEmail);
        BudgetCategory budgetCategory = budgetCategoryService.getBudgetCategoryById(budgetCategoryId);

        Expense expense = modelMapper.map(expenseDto, Expense.class);

        expense.setUser(user);
        expense.setBudgetCategory(budgetCategory);
        expense.setDate(LocalDate.now());

        return ResponseEntity.ok().body(expenseService.saveExpense(expense));
    }
}
