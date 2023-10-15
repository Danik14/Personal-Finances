package slash.financing.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import slash.financing.data.User;
import slash.financing.enums.UserRole;
import slash.financing.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExistsByEmail() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        boolean result = userService.doesUserExistByEmail("test@example.com");

        assertTrue(result);
    }

    @Test
    public void testExistsByName() {
        when(userRepository.existsByName("John")).thenReturn(true);

        boolean result = userService.doesUserExistByName("John");

        assertTrue(result);
    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        var result = userService.getUserByEmail("test@example.com");

        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void testVerifyEmail() {
        String email = "test@example.com";
        UserRole verifiedRole = UserRole.VERIFIED_USER;
        when(userRepository.verifyEmail(email, verifiedRole)).thenReturn(1);

        int result = userService.verifyUserEmail(email);

        assertEquals(1, result);
    }
}
