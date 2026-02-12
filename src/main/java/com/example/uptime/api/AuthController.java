package com.example.uptime.api;

import com.example.uptime.api.dto.AuthDto;
import com.example.uptime.domain.User;
import com.example.uptime.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import static com.example.uptime.api.dto.AuthDto.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthDto.RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.status(409).body("Username already taken");
        }

        User user = new User()
                .setUsername(request.username())
                .setPasswordHash(passwordEncoder.encode(request.password()));

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto.LoginRequest request,
                                   HttpServletRequest httpServletRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        httpServletRequest
                .getSession(true)
                .setAttribute(
                        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        SecurityContextHolder.getContext()
                );

        return ResponseEntity.ok().build();
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession != null) httpSession.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(401).build();
        }

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow();

        return ResponseEntity.ok(new MeResponse(user.getId(), authentication.getName()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody AuthDto.ChangePasswordRequest changePasswordRequest,
                                            Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(401).build();
        }

        String username = authentication.getName();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                    changePasswordRequest.oldPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Current password is incorrect");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setPasswordHash(passwordEncoder.encode(changePasswordRequest.newPassword()));
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }

}
