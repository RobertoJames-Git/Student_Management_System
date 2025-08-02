package com.roberto.studentmanagement.Student.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ViewController {


    @GetMapping({"/"})
    public String homePage(){
        return "index";
    }

    @GetMapping("/studentLogin")
    public String studentLogin(){
        return "studentLogin";
    }

    @GetMapping("/adminLogin")
    public String adminLogin(){
        return "adminLogin";
    }

    @GetMapping("/adminDashboard")
    public String adminDashboard(){
        return "adminDashboard";
    }
}
