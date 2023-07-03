package slash.financing.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import slash.financing.data.User;
import slash.financing.dto.UserUpdateDto;
import slash.financing.service.UserService;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    // private final ModelMapper mapper;
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<User> getOne(@PathVariable String uuid) {
        UUID id = UUID.fromString(uuid);
        if (uuid == null) {
            throw new IllegalArgumentException("Invalid UUID format");
        }

        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<?> updateUser(@PathVariable UUID uuid, @RequestBody UserUpdateDto userUpdateDto,
            Principal principal) {
        String userEmail = principal.getName();
        User user = userService.getUserById(uuid);

        // Check if the email is being updated to a different value
        if (!user.getEmail().equals(userEmail)) {
            log.error(userEmail + " " + user.getEmail());

            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Only user who created this account can change its username!");
        }

        return ResponseEntity.ok().body(userService.updateUser(user, userUpdateDto));

    }

    // @PostMapping
    // public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto,
    // BindingResult bindingResult,
    // UriComponentsBuilder uriBuilder) {
    // if (bindingResult.hasErrors()) {
    // // Handle validation errors
    // Map<String, String> errors = new HashMap<>();
    // for (FieldError error : bindingResult.getFieldErrors()) {
    // errors.put(error.getField(), error.getDefaultMessage());
    // }
    // return ResponseEntity.badRequest().body(errors);
    // }

    // User user = mapper.map(userDto, User.class);
    // User createdUser = service.createUser(user);

    // // Build the URI for the created resource
    // UriComponents uriComponents =
    // uriBuilder.path("/users/{id}").buildAndExpand(createdUser.getId());
    // URI createdUri = uriComponents.toUri();

    // return ResponseEntity.created(createdUri).body(createdUser);
    // }

    public ResponseEntity<?> deleteUser(@PathVariable String uuid) {
        UUID id = UUID.fromString(uuid);
        if (uuid == null) {
            throw new IllegalArgumentException("Invalid UUID format");
        }

        userService.deleteUser(id);

        return ResponseEntity.ok().body("User was successfuly deleted");
    }

}
