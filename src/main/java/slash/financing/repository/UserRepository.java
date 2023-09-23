package slash.financing.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import slash.financing.data.User;
import slash.financing.enums.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    public boolean existsByEmail(String email);

    public boolean existsByName(String name);

    public Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.role = :verifiedRole WHERE u.email = :email")
    int verifyEmail(String email, UserRole verifiedRole);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.friends = :friends WHERE u.id = :userId")
    void addFriendToUser(@Param("userId") UUID userId, @Param("friends") List<User> friends);
}
