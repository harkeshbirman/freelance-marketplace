package com.marketplace.auth.service;

import com.marketplace.auth.dto.UserRequest;
import com.marketplace.auth.entity.Role;
import com.marketplace.auth.entity.User;
import com.marketplace.auth.exception.UserAlreadyExistsException;
import com.marketplace.auth.repository.RoleRepository;
import com.marketplace.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public Long registerNewUser(UserRequest userRequest) {
        log.info("Registering new user with email={}", userRequest.getEmail());

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            log.warn("Registration failed: Email {} is already in use", userRequest.getEmail());
            throw new UserAlreadyExistsException("Email is already in use");
        }

        Role role = null;
        String requestedRole = userRequest.getRole();
        if (requestedRole != null) {
            log.debug("Requested role: {}", requestedRole);
        } else {
            log.warn("Requested role is null; defaulting to CLIENT");
        }

        if (requestedRole != null && (requestedRole.equalsIgnoreCase("freelancer") || requestedRole.equalsIgnoreCase("client"))) {
            String roleName = "ROLE_" + requestedRole.toUpperCase();
            role = roleRepository
                    .findByRoleName(roleName)
                    .orElseGet(() -> {
                        log.info("Role {} not found, creating new", roleName);
                        return roleRepository.save(new Role(null, roleName));
                    });
        } else {
            log.info("Invalid or missing role '{}', defaulting to ROLE_CLIENT", requestedRole);
            role = roleRepository.save(new Role(null, "ROLE_CLIENT"));
        }

        log.debug("Using role: {}", role.getRoleName());

        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(role)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with id={}", savedUser.getId());

        return savedUser.getId();
    }

}
