package slash.financing.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import slash.financing.enums.UserRole;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Builder.Default
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name = "username", length = 35, nullable = false)
    @Length(min = 3, max = 35)
    private String username;

    @Column(name = "email", nullable = false)
    @Email
    private String email;

    @Column(name = "hashed_password", nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "user_budget_categories", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "budget_category_id"))
    private Set<BudgetCategory> budgetCategories = new HashSet<>();

    // @Column(name = "is_verified")
    // private boolean isVerified;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }
}
