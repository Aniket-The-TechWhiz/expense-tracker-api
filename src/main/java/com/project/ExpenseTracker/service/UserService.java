package com.project.ExpenseTracker.service;

import com.project.ExpenseTracker.dto.RegisterRequest;
import com.project.ExpenseTracker.dto.UserResponse;
import com.project.ExpenseTracker.model.User;
import com.project.ExpenseTracker.repository.UserRepository;
import com.project.ExpenseTracker.security.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    public UserResponse register(RegisterRequest request) {
        User user=User.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        User savedUser=userRepository.save(user);
        return mapToResponse(savedUser);
    }

    private UserResponse mapToResponse(User savedUser) {
        UserResponse userResponse=new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setUserName(savedUser.getUserName());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        return userResponse;
    }

    public String login(@Valid RegisterRequest request) {
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        return jwtUtils.generateToken(user.getUserName(), user.getId().intValue());
    }
}
