package slash.financing.dto.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

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
public class ExpenseDto {

    @JsonProperty(access = Access.READ_ONLY)
    private UUID id;

    @JsonProperty(access = Access.READ_ONLY)
    private UUID budgetCategoryId;

    @Positive
    @NotNull
    private BigDecimal amount;

    @Default
    @Length(min = 0, max = 300, message = "description must be between 0 and 300")
    @NotNull
    private String description = "";

    @JsonProperty(access = Access.READ_ONLY)
    private String budgetCategoryName;

    @JsonProperty(access = Access.READ_ONLY)
    private LocalDate date;
}
