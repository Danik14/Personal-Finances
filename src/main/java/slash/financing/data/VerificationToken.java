package slash.financing.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken {

    @Id
    @SequenceGenerator(name = "verification_tokens_sequence", sequenceName = "verification_tokens_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_tokens_sequence")
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime verifiedAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "users_id")
    private User user;

    public VerificationToken(String token,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}
