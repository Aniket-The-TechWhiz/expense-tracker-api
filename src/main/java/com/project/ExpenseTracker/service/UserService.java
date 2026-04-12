package com.project.ExpenseTracker.service;

import com.project.ExpenseTracker.dto.RegisterRequest;
import com.project.ExpenseTracker.dto.UserResponse;
import com.project.ExpenseTracker.model.User;
import com.project.ExpenseTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public UserResponse register(RegisterRequest request) {
        User user=User.builder()
                .userName(request.getUserName())
                .password(request.getPassword())
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
}
