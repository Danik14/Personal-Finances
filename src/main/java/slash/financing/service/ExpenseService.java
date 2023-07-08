package slash.financing.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import slash.financing.data.Expense;
import slash.financing.data.User;

public interface ExpenseService {
    List<Expense> getAllUserExpenses(User user);

    Expense getExpenseById(UUID id);

    Expense saveExpense(Expense expense);

    BigDecimal getTotalMoneySpentForUserInDateRange(User user, LocalDate startDate, LocalDate endDate);

    List<Expense> getExpensesForUserInDateRange(User user, LocalDate startDate, LocalDate endDate);

    BigDecimal getTotalMoneySpentForUser(User user);

}
