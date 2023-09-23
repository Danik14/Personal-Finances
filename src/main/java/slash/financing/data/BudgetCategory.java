package slash.financing.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "budget_categories")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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
    @Column(name = "is_personal", nullable = false)
    private boolean isPersonal = true;

    public boolean isPersonal() {
        return isPersonal;
    }

    @Builder.Default
    @JsonBackReference
    @ManyToMany(mappedBy = "budgetCategories")
    @ToString.Exclude
    private List<User> users = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        BudgetCategory that = (BudgetCategory) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
