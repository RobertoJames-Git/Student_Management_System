package com.roberto.studentmanagement.Student.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {


    private Integer studentID;

    @NotBlank(message = "First Name is required")
    private String fname;

    @NotBlank(message = "Last Name is required")
    private String lname;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotBlank(message = "Date of Birth is required")

    private String dob;

    @NotBlank(message = "Password is required")
    private String password;

}
