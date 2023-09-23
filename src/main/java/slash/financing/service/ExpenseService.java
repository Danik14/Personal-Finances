package slash.financing.service;

import slash.financing.data.BudgetCategory;
import slash.financing.data.Expense;
import slash.financing.data.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ExpenseService {
    List<Expense> getAllUserExpenses(User user);

    Expense getExpenseById(UUID id);

    Expense saveExpense(Expense expense);

    BigDecimal getTotalMoneySpentForUserInDateRange(User user, LocalDate startDate, LocalDate endDate);

    List<Expense> getExpensesForUserInDateRange(User user, LocalDate startDate, LocalDate endDate);

    BigDecimal getTotalMoneySpentForUser(User user);

    List<Expense> getByBudgetCategory(BudgetCategory budgetCategory);

    List<Expense> getByBudgetCategoryAndDateBetween(BudgetCategory budgetCategory, LocalDate startDate,
            LocalDate endDate);

    BigDecimal getTotalMoneySpentForBudgetCategory(BudgetCategory budgetCategory);

    BigDecimal getTotalMoneySpentForBudgetCategoryInDateRange(BudgetCategory budgetCategory, LocalDate startDate,
            LocalDate endDate);
}
