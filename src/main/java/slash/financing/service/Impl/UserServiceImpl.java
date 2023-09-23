package slash.financing.service.Impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import slash.financing.data.User;
import slash.financing.dto.User.UserUpdateDto;
import slash.financing.enums.UserRole;
import slash.financing.exception.UserNotFoundException;
import slash.financing.mapper.UserMapper;
import slash.financing.repository.UserRepository;
import slash.financing.service.UserService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserMapper userMapper;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with such email not found"));
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with such id not found"));
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with such id not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public int verifyUserEmail(String email) {
        return userRepository.verifyEmail(email, UserRole.VERIFIED_USER);
    }

    @Override
    public User updateUser(UserUpdateDto userUpdateDto) {
        return userRepository.save(userMapper.updateDtoToEntity(userUpdateDto));
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
