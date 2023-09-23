package slash.financing.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import slash.financing.dto.AuthenticationRequestDto;
import slash.financing.dto.AuthenticationResponse;
import slash.financing.dto.RegistrationRequest;
import slash.financing.service.AuthenticationService;
import slash.financing.service.VerificationTokenService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    private final VerificationTokenService verificationTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            // setting required to false to then check if it is null (empty)
            @Valid @RequestBody(required = false) RegistrationRequest registerRequest,
            BindingResult bindingResult,
            UriComponentsBuilder uriBuilder) {

        if (registerRequest == null) {
            // Create a custom error message
            Map<String, String> error = new HashMap<>();
            error.put("message", "Request body is missing");
            return ResponseEntity.badRequest().body(error);
        }

        if (bindingResult.hasErrors()) {
            // Handle validation errors
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        // User user = mapper.map(userDto, User.class);
        AuthenticationResponse response = authenticationService.register(registerRequest);

        // Build the URI for the created resource
        // UriComponents uriComponents =
        // uriBuilder.path("/users/{id}").buildAndExpand(createdUser.getId());
        // URI createdUri = uriComponents.toUri();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody(required = false) AuthenticationRequestDto request,
            BindingResult bindingResult) {

        if (request == null) {
            // Create a custom error message
            Map<String, String> error = new HashMap<>();
            error.put("message", "Request body is missing");
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping(path = "/verify")
    public String verify(@RequestParam("token") String token) {
        return verificationTokenService.verifyToken(token);
    }
}
