package slash.financing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetCategoryDto {
    @JsonProperty(access = Access.READ_ONLY)
    private UUID id;

    @Length(min = 3, max = 35, message = "name must be between 3 and 35")
    @NotNull
    public String name;

    @Length(min = 5, max = 300, message = "description must be between 5 and 300")
    @NotNull
    private String description;
}
