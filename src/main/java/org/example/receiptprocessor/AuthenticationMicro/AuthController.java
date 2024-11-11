package org.example.receiptprocessor.AuthenticationMicro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login/{username}/{password}")
    public Map<String, String> login(@PathVariable String username, @PathVariable String password) {
        // For demonstration purposes
        if ("123".equals(username) && "123".equals(password)) {
            String token = jwtUtil.generateToken(username);  // Generate JWT token for the user
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return response;
        }

        throw new RuntimeException("Invalid credentials");
    }
}
