package com.roberto.studentmanagement.Student.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Repository
public class LoginRequest {

    @NotBlank(message="Email is required")
    private String email;

    @NotBlank(message="Password is required")
    private String password;
}
