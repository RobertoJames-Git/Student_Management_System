package com.roberto.studentmanagement.Student.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {



    @GetMapping("/studentLogin")
    public String studentLogin(){
        return "studentLogin";
    }

}
