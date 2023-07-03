package slash.financing.dto;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetCategoryUpdateDto {
    @Length(min = 3, max = 35, message = "name must be between 3 and 35")
    public String name;

    @Length(min = 5, max = 300, message = "message must be between 5 and 300")
    private String description;
}
