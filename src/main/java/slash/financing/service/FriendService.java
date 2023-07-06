package slash.financing.service;

import java.util.List;

import slash.financing.data.User;

public interface FriendService {
    List<User> getAllFriends(User user);

    void addFriendToUser(User user, User friend);

    void deleteFriend(User owner, User friend);

}
