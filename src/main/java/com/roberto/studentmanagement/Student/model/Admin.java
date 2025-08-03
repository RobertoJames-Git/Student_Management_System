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
public class Admin {
    @NotBlank(message= "Field is required")
    private String email;
    private String fname;
    private String lname;
    @NotBlank(message = "Field is required")
    private String password;
}
