package slash.financing.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import slash.financing.data.User;
import slash.financing.repository.UserRepository;
import slash.financing.service.FriendService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAllFriends(User user) {
        throw new UnsupportedOperationException("Unimplemented method 'getAllFriends'");
    }

    @Override
    @Transactional
    public void addFriendToUser(User user, User friend) {
        user.getFriends().add(friend);
        friend.getFriends().add(user);
        userRepository.save(user);
        userRepository.save(friend);
    }

    @Override
    public void deleteFriend(User owner, User friend) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFriend'");
    }

}
