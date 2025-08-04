package com.roberto.studentmanagement.Student.controller;

import com.roberto.studentmanagement.Student.model.Admin;
import com.roberto.studentmanagement.Student.model.LoginRequest;
import com.roberto.studentmanagement.Student.model.Student;
import com.roberto.studentmanagement.Student.service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {


    private final AdminService adminService;
    private HttpSession httpSession;

    public AdminController(AdminService adminService, HttpSession httpSession){
        this.adminService = adminService;
        this.httpSession = httpSession;
    }

    @PostMapping("/addStudent")
    public ResponseEntity<String> addStudent(@Valid @RequestBody Student student){

        Map<Boolean,String> result = new HashMap<>();
        result = adminService.addStudent(student);

        //checks if there were any issues adding student record
        if(result.get(false)!=null){
            //returns error message to user
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.get(false));
        }
        // return success message
        else {
            return ResponseEntity.ok(result.get(true));
        }
    }

    @GetMapping("/getAllStudents")
    public List<Student> getAllStudents(){
        return adminService.getAllStudents();
    }

    @DeleteMapping("/deleteStudent")
    public ResponseEntity<String> deleteStudent(@RequestParam int studentID){
        return adminService.deleteStudent(studentID);
    }

    @PostMapping("/verifyAdminCredentials")
    public ResponseEntity<?> verifyAdminCredentials(@Valid @RequestBody LoginRequest loginRequest) {

        Admin verifiedAdmin = adminService.verifyAdminCredentials(loginRequest);

        if (verifiedAdmin != null) {
            // Set session attributes
            httpSession.setAttribute("adminFullName", verifiedAdmin.getFname() + " " + verifiedAdmin.getLname());
            httpSession.setAttribute("adminEmail", verifiedAdmin.getEmail());

            // Return redirect path to frontend
            return ResponseEntity.ok("/adminDashboard");
        }

        // Return error if credentials invalid
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
    }

    @PostMapping("/addAdmin")
    public ResponseEntity<String>addAdmin( @RequestBody Admin admin){

        Map<Boolean, String> response =adminService.addAdmin(admin);

        if(response.get(false)!=null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.get(false));
        }
        else{
            return ResponseEntity.ok(response.get(true));
        }
    }

   /* @GetMapping('/getAllModules')
    public List<Module> getAllModules(){

    }
*/

}
