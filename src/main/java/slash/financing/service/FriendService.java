package slash.financing.service;

import slash.financing.data.User;

import java.util.List;

public interface FriendService {
    List<User> getAllFriends(User user);

    void addFriendToUser(User user, User friend);

    void deleteFriend(User owner, User friend);

}
