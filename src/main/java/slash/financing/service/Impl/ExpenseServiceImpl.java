package slash.financing.service.Impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import slash.financing.data.Expense;
import slash.financing.data.User;
import slash.financing.exception.ExpenseNotFoundException;
import slash.financing.repository.ExpenseRepository;
import slash.financing.service.ExpenseService;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    public List<Expense> getUserExpenses(User user) {
        return expenseRepository.findByUser(user);
    }

    public Expense getExpenseById(UUID id) {
        return expenseRepository.findById(id).orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
    }

    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public BigDecimal getTotalMoneySpentForUserInDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.getTotalMoneySpentForUserInDateRange(user, startDate, endDate);
    }

    public List<Expense> getExpensesForUserInDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.getExpensesForUserInDateRange(user, startDate, endDate);
    }
}
