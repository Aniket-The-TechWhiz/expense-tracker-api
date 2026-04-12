package com.project.ExpenseTracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "userName is Required")
    private String userName;
    @NotBlank(message = "password is Required")
    private String password;
}
