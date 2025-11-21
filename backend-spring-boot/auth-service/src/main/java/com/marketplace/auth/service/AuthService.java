package com.marketplace.auth.service;

import com.marketplace.auth.dto.JwtResponse;
import com.marketplace.auth.dto.LoginRequest;
import com.marketplace.auth.dto.TokenVerificationResponse;
import com.marketplace.auth.entity.User;
import com.marketplace.auth.exception.UserNotFoundException;
import com.marketplace.auth.jwt.JwtHelper;
import com.marketplace.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;

    public JwtResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        log.info("Login attempt for email={}", email);
        doAuthenticate(email, password);

        User user = getUserByEmail(email);
        String token = jwtHelper.generateToken(user);
        log.info("Token generated for userId={}", user.getId());

        return new JwtResponse(token, user.getName(), user.getRole().getRoleName().substring(5), user.getId(), user.getEmail());
    }

    public TokenVerificationResponse verifyToken(String token) {
        log.info("Verifying token");
        String email = jwtHelper.verifyToken(token);
        log.debug("Token verified, extracted email={}", email);
        User user = getUserByEmail(email);

        return new TokenVerificationResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().getRoleName());
    }

    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        try {
            authenticationManager.authenticate(authenticationToken);
            log.debug("Authentication successful for user={}", username);
        } catch (BadCredentialsException e) {
            log.warn("Authentication failed for user={}: {}", username, e.getMessage());
            throw new BadCredentialsException("Invalid Username or Password");
        } catch (Exception e) {
            log.error("Unexpected error during authentication for user={}", username, e);
            throw e;
        }
    }

    private User getUserByEmail(String email) {
        log.debug("Fetching user by email={}", email);
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            log.error("User not found with email={}", email);
            throw new UserNotFoundException("User not found with email: " + email);
        }
        return opt.get();
    }
}
