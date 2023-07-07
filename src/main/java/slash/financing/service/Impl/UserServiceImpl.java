package slash.financing.service.Impl;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import slash.financing.data.User;
import slash.financing.dto.User.UserUpdateDto;
import slash.financing.enums.UserRole;
import slash.financing.exception.UserNotFoundException;
import slash.financing.repository.UserRepository;
import slash.financing.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

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
    public User updateUser(User user, UserUpdateDto userUpdateDto) {
        modelMapper.map(userUpdateDto, user);

        return userRepository.save(user);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
