package slash.financing.dto.Expense;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseCreateDto {

    @NotNull
    private UUID budgetCategoryId;

    @Positive
    @NotNull
    private BigDecimal amount;

    @Default
    @Length(min = 0, max = 300, message = "description must be between 0 and 300")
    @NotNull
    private String description = "";
}
