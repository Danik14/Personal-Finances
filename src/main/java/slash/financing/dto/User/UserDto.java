package slash.financing.dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import slash.financing.dto.BudgetCategoryDto;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String name;
    private String email;

    private List<BudgetCategoryDto> budgetCategories;

    private List<FriendDto> friends;
}