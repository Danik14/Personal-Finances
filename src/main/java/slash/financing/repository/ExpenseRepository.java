package slash.financing.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import slash.financing.data.Expense;
import slash.financing.data.User;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
        List<Expense> findByUser(User user);

        @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.date BETWEEN :startDate AND :endDate")
        BigDecimal getTotalMoneySpentForUserInDateRange(@Param("user") User user,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.date BETWEEN :startDate AND :endDate")
        List<Expense> getExpensesForUserInDateRange(@Param("user") User user, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user")
        BigDecimal getTotalMoneySpentForUser(@Param("user") User user);

        @Query("SELECT e FROM Expense e WHERE e.user = :user")
        List<Expense> getExpensesForUser(@Param("user") User user);
}