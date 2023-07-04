package slash.financing.data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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

    public static final String DEFAULT_CATEGORY_FOOD = "Food";
    public static final String DEFAULT_CATEGORY_TRANSPORTATION = "Transportation";
    public static final String DEFAULT_CATEGORY_HOUSING = "Housing";
    public static final String DEFAULT_CATEGORY_ENTERTAINMENT = "Entertainment";
    public static final String DEFAULT_CATEGORY_HEALTHCARE = "Healthcare";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 35, nullable = false)
    @Length(min = 3, max = 35)
    private String name;

    @Column(name = "description", length = 300, nullable = false)
    @Length(min = 5, max = 300)
    private String description;

    @Builder.Default
    @JsonBackReference
    @ManyToMany(mappedBy = "budgetCategories", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();
}
