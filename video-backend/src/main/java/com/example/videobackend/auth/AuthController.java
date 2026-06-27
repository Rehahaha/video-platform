package com.example.videobackend.auth;

import com.example.videobackend.auth.dto.LoginRequest;
import com.example.videobackend.auth.dto.LoginResponse;
import com.example.videobackend.auth.dto.RegisterRequest;
import com.example.videobackend.auth.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = authService.register(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    // 先简化：通过 query 参数传 userId 获取用户信息
    // 后续可以改为从 JWT 中解析 userId
    @GetMapping("/profile")
    public UserResponse profile(@RequestParam("userId") Long userId) {
        return authService.getProfile(userId);
    }
}



