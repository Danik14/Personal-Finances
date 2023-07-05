package slash.financing.service;

import java.util.List;
import java.util.UUID;

import slash.financing.data.Expense;
import slash.financing.data.User;

public interface ExpenseService {
    List<Expense> getUserExpenses(User user);

    Expense getExpenseById(UUID id);

    Expense saveExpense(Expense expense);

}
