package slash.financing.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import slash.financing.data.Expense;
import slash.financing.data.User;
import slash.financing.repository.ExpenseRepository;
import slash.financing.repository.UserRepository;

@RestController
@RequestMapping("api/v1/expenses")
@RequiredArgsConstructor
@Slf4j
public class ExpenseController {
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    @GetMapping("")
    public ResponseEntity<List<Expense>> getUserExpenses(Principal principal) {
        log.info("1");
        String userEmail = principal.getName();
        log.info("2");

        User user = userRepository.findByEmail(userEmail).orElse(null);
        log.info("3");

        List<Expense> expenses = expenseRepository.findByUser(user);
        log.info("4");

        var response = ResponseEntity.ok().body(expenses);

        log.info("1");

        return response;
    }
}
