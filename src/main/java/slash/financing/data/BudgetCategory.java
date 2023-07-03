package slash.financing.data;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "budget_categories")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username", length = 35, nullable = false)
    @Length(min = 3, max = 35)
    private String name;

    @Column(name = "username", length = 300, nullable = false)
    @Length(min = 5, max = 300)
    private String description;
}
