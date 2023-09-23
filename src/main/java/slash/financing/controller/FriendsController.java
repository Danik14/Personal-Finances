package slash.financing.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import slash.financing.data.User;
import slash.financing.service.FriendService;
import slash.financing.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users/friends")
@RequiredArgsConstructor
@Slf4j
@Validated
public class FriendsController {
    private final UserService userService;
    private final FriendService friendService;

    @GetMapping("")
    public ResponseEntity<List<User>> getAll(Principal principal) {
        String userEmail = principal.getName();
        User user = userService.getUserByEmail(userEmail);

        return ResponseEntity.ok().body(user.getFriends());
    }

    @PostMapping("/{uuid}")
    public ResponseEntity<User> addFriend(@PathVariable UUID uuid, Principal principal) {
        String userEmail = principal.getName();
        User user = userService.getUserByEmail(userEmail);
        User friend = userService.getUserById(uuid);

        friendService.addFriendToUser(user, friend);

        return ResponseEntity.ok().body(friend);
    }


//    @DeleteMapping("/{uuid}")
//    public ResponseEntity<?> deleteFriend(@PathVariable UUID uuid) {
//        userService.deleteUser(uuid);
//
//        return ResponseEntity.ok().body("User was successfuly deleted");
//    }

}
