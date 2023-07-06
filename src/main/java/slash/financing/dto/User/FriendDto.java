package slash.financing.dto.User;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import slash.financing.dto.BudgetCategoryDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendDto {
    private UUID id;
    private String username;
    private String email;

    private List<BudgetCategoryDto> budgetCategories;
}