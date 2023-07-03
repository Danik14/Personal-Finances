package slash.financing.data;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBudgetCategoryId implements Serializable {
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "budget_category_id")
    private UUID budgetCategoryId;
}