package slash.financing.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import slash.financing.data.User;
import slash.financing.dto.User.UserDto;
import slash.financing.dto.User.UserUpdateDto;
import slash.financing.enums.UserRole;
import slash.financing.service.UserService;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final ModelMapper modelMapper;
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserDto>> getAll() {
        List<User> users = userService.getAllUsers();

        List<UserDto> userResponse = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = modelMapper.map(user, UserDto.class);
            userResponse.add(userDto);
        }

        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getOne(@PathVariable String uuid, Principal principal) {
        UUID id = UUID.fromString(uuid);
        if (uuid == null) {
            throw new IllegalArgumentException("Invalid UUID format");
        }

        String userEmail = principal.getName();
        User loggedInUser = userService.getUserByEmail(userEmail);

        if (loggedInUser.getRole() == UserRole.ADMIN || loggedInUser.getId().equals(id)) {
            // If admin or accessing own ID, return the full user information
            User user = userService.getUserById(id);
            return ResponseEntity.ok().body(user);
        } else {
            // If regular user trying to access someone else's ID, return limited user
            // information
            User user = userService.getUserById(id);
            UserDto userDto = modelMapper.map(user, UserDto.class);

            return ResponseEntity.ok().body(userDto);
        }
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<?> updateUser(@PathVariable UUID uuid, @RequestBody UserUpdateDto userUpdateDto,
            Principal principal) {
        String userEmail = principal.getName();
        User user = userService.getUserById(uuid);

        // Check if the email is being updated to a different value
        if (!user.getEmail().equals(userEmail) || user.getRole() != UserRole.ADMIN) {
            log.error(userEmail + " " + user.getEmail());

            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Only user who created this account can change its username!");
        }

        return ResponseEntity.ok().body(userService.updateUser(user, userUpdateDto));

    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);

        return ResponseEntity.ok().body("User was successfuly deleted");
    }

}
