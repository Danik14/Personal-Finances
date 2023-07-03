package slash.financing.data;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_budget_categories")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBudgetCategory {
    @EmbeddedId
    private UserBudgetCategoryId id;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("budgetCategoryId")
    private BudgetCategory budgetCategory;
}