package com.roberto.studentmanagement.Student.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Repository
public class Module {

    @NotBlank(message = "Module Code is required")
    private String moduleCode;
    @NotBlank(message = "Module Name is required")
    private String moduleName;
    @NotNull(message = "Credits is required")
    @Min(value = 1, message = "Min credit is 1")
    @Max(value = 4, message = "Max credit is 4")
    private Integer credits;

    private String added_by;
}
