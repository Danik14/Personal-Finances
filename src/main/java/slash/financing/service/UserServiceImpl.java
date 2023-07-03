package slash.financing.service;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import slash.financing.data.User;
import slash.financing.dto.UserUpdateDto;
import slash.financing.enums.UserRole;
import slash.financing.exception.UserNotFoundException;
import slash.financing.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    // private final PasswordEncoder passwordEncoder;

    // public User createUser(User user) {
    // if (repository.existsByEmail(user.getEmail())) {
    // throw new UserAlreadyExistsException("User with such email already exists");
    // } else if (repository.existsByUsername(user.getUsername())) {
    // throw new UserAlreadyExistsException("User with such username already
    // exists");
    // }

    // user.setPassword(passwordEncoder.encode(user.getPassword()));

    // return repository.save(user);
    // }

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
}
