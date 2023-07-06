package slash.financing.service;

import java.util.List;
import java.util.UUID;

import slash.financing.data.User;
import slash.financing.dto.User.UserUpdateDto;

public interface UserService {
    User saveUser(User user);

    List<User> getAllUsers();

    User getUserByEmail(String email);

    User getUserById(UUID id);

    void deleteUser(UUID id);

    int verifyUserEmail(String email);

    User updateUser(User user, UserUpdateDto userUpdateDto);
}
