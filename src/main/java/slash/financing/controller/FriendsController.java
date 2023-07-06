package slash.financing.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import slash.financing.data.User;
import slash.financing.service.FriendService;
import slash.financing.service.UserService;

@RestController
@RequestMapping("api/v1/users/friends")
@RequiredArgsConstructor
@Slf4j
public class FriendsController {
    // private final ModelMapper mapper;
    private final UserService userService;
    private final FriendService friendService;

    @GetMapping("")
    public ResponseEntity<List<User>> getAll(Principal principal) {
        String userEmail = principal.getName();
        User user = userService.getUserByEmail(userEmail);

        return ResponseEntity.ok().body(user.getFriends());
    }

    @PostMapping("/{uuid}")
    public ResponseEntity<User> addFriend(@PathVariable String uuid, Principal principal) {
        UUID friendId = UUID.fromString(uuid);
        if (uuid == null) {
            throw new IllegalArgumentException("Invalid UUID format");
        }

        String userEmail = principal.getName();
        User user = userService.getUserByEmail(userEmail);
        User friend = userService.getUserById(friendId);

        try {
            friendService.addFriendToUser(user, friend);
        } catch (Exception e) {
            log.error("Unable to save the user: ", e);
        }

        return ResponseEntity.ok().body(friend);
    }

    public ResponseEntity<?> deleteUser(@PathVariable String uuid) {
        UUID id = UUID.fromString(uuid);
        if (uuid == null) {
            throw new IllegalArgumentException("Invalid UUID format");
        }

        userService.deleteUser(id);

        return ResponseEntity.ok().body("User was successfuly deleted");
    }

}
