package slash.financing.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import slash.financing.data.Expense;
import slash.financing.data.User;
import slash.financing.dto.Expense.ExpenseResponseDto;
import slash.financing.service.ExpenseService;
import slash.financing.service.UserService;

@RestController
@RequestMapping("api/v1/expenses")
@RequiredArgsConstructor
// @Slf4j
public class ExpenseController {
    private final UserService userService;
    private final ExpenseService expenseService;

    @GetMapping("")
    public ResponseEntity<List<ExpenseResponseDto>> getUserExpenses(Principal principal) {
        String userEmail = principal.getName();
        User user = userService.getUserByEmail(userEmail);
        List<Expense> expenses = expenseService.getUserExpenses(user);

        List<ExpenseResponseDto> expenseResponses = expenses.stream()
                .map(expense -> ExpenseResponseDto.builder()
                        .id(expense.getId())
                        .amount(expense.getAmount())
                        .date(expense.getDate())
                        .description(expense.getDescription())
                        .budgetCategory(expense.getBudgetCategory().getName())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(expenseResponses);

    }

    @PostMapping("")
    public ResponseEntity<?> addUserExpense(@Valid @RequestBody Expense expense, Principal principal,
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

        List<Expense> expenses = expenseService.getUserExpenses(user);
        return ResponseEntity.ok().body(expenses);
    }
}
