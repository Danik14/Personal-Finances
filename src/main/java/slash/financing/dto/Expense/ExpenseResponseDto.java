package slash.financing.dto.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponseDto {
    private UUID id;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private String budgetCategory;
}
