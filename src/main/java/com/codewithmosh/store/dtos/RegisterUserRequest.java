package com.codewithmosh.store.dtos;

import com.codewithmosh.store.validation.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Character must be less than 255")
    private String name;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 25, message = "password must be between 6 and 25 characters long")
    private String password;

    @NotBlank(message = "email is required")
    @Email(message = "email format must be valid")
    @Lowercase(message = "email must be in lowercase")
    private String email;
}
