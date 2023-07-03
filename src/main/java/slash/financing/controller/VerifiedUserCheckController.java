package slash.financing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/verified")
public class VerifiedUserCheckController {
    @GetMapping("/{name}")
    public ResponseEntity<String> adminSomething(@PathVariable String name) {
        return ResponseEntity.ok().body("Hello, verified user " + name);
    }
}
