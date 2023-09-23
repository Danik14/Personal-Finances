package slash.financing.service;

import slash.financing.data.User;
import slash.financing.dto.User.UserUpdateDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User saveUser(User user);

    List<User> getAllUsers();

    User getUserByEmail(String email);

    User getUserById(UUID id);

    void deleteUser(UUID id);

    int verifyUserEmail(String email);

    User updateUser(UserUpdateDto userUpdateDto);
}
