package slash.financing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import slash.financing.data.VerificationToken;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface VerificationTokenRepository
                extends JpaRepository<VerificationToken, Long> {

        Optional<VerificationToken> findByToken(String token);

        @Transactional
        @Modifying
        @Query("UPDATE VerificationToken c " +
                        "SET c.verifiedAt = ?2 " +
                        "WHERE c.token = ?1")
        int updateVerifiedAt(String token,
                        LocalDateTime confirmedAt);
}