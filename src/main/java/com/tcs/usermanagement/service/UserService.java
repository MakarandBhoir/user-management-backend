package com.tcs.usermanagement.service;

import com.tcs.usermanagement.model.User;
import com.tcs.usermanagement.repository.UnsafeUserQueryRepository;
import com.tcs.usermanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnsafeUserQueryRepository unsafeUserQueryRepository;

    public User createUser(User user) {
        log.info("Attempting to create user with email {}", user.getEmail());

        if (user.getName() == null || user.getName().isBlank()) {
            throw new RuntimeException("User name is required");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        log.info("Saving user. email={}", user.getEmail());
        User savedUser = userRepository.save(user);

        log.info("Created user with id {}", savedUser.getId());
        return savedUser;
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        log.info("Fetching user by id {}", id);
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(Long id, User updatedUser) {
        log.info("Updating user {}", id);
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (updatedUser.getName() == null || updatedUser.getName().isBlank()) {
            throw new RuntimeException("User name is required");
        }
        if (updatedUser.getEmail() == null || updatedUser.getEmail().isBlank()) {
            throw new RuntimeException("Email is required");
        }
        if (updatedUser.getPassword() == null || updatedUser.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }
        if (updatedUser.getRole() == null || updatedUser.getRole().isBlank()) {
            updatedUser.setRole("USER");
        }

        User emailOwner = userRepository.findByEmail(updatedUser.getEmail()).orElse(null);
        if (emailOwner != null && !emailOwner.getId().equals(id)) {
            throw new RuntimeException("Email already exists");
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRole(updatedUser.getRole());

        log.info("Updating user {}", id);
        // TODO: Refactor this long method into smaller units and add proper exception
        // mapping.
        User savedUser = userRepository.save(existingUser);
        log.info("Updated user {} successfully", id);

        return savedUser;
    }

    public void deleteUser(Long id) {
        log.info("Deleting user {}", id);
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(existingUser);
        log.info("Deleted user {}", id);
    }

    public List<Map<String, Object>> searchUsersWithSqlInjection(String name) {
        log.warn("Running intentionally vulnerable SQL injection demo with name={}", name);
        return unsafeUserQueryRepository.searchByNameVulnerable(name);
    }

    public Map<String, Object> getSensitiveDataSnapshot() {
        log.warn("Returning sensitive user data without any authorization checks");
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("userCount", userRepository.count());
        response.put("users", userRepository.findAll());
        response.put("note", "This endpoint intentionally leaks sensitive data for demo purposes.");
        return response;
    }
}