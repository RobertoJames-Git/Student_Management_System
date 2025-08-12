package com.roberto.studentmanagement.Student.controller;

import com.roberto.studentmanagement.Student.Session.ClearAllSessions;
import com.roberto.studentmanagement.Student.repository.AdminRepository;
import com.roberto.studentmanagement.Student.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ViewController {

    private final HttpSession httpSession;
    private final AdminService adminService;

    public ViewController(AdminService adminService, HttpSession httpSession){
        this.httpSession = httpSession;
        this.adminService= adminService;
    }

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
    public String adminDashboard(Model model){

        String adminEmail=(String)httpSession.getAttribute("adminEmail");

        //check if the user has a session with an email or the email corresponds to a admin in the database
        if(adminEmail == null || !adminService.adminEmailExist(adminEmail)){
            return"/adminLogin";//redirect user to login form if they are not logged in
        }

        model.addAttribute("adminFullName", (String) httpSession.getAttribute("adminFullName") );
        return "adminDashboard";
    }

    @GetMapping("/addManyStudents")
    public String addManyStudents(){
        return "addManyStudents";
    }


}
