package com.roberto.studentmanagement.Student.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Repository
public class Module {
    private Integer moduleCode;
    private String moduleName;
    private int credits;
    private int added_by;
}
