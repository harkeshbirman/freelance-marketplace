package com.marketplace.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotNull
    @NotBlank
    private String role;
}
