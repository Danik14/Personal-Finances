package slash.financing.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetCategoryDto {
    @Length(min = 3, max = 35, message = "name must be between 3 and 35")
    @NotNull
    public String name;

    @Length(min = 5, max = 300, message = "description must be between 5 and 300")
    @NotNull
    private String description;
}
