package com.roberto.studentmanagement.Student.controller;

import com.roberto.studentmanagement.Student.model.Admin;
import com.roberto.studentmanagement.Student.model.LoginRequest;
import com.roberto.studentmanagement.Student.model.Student;
import com.roberto.studentmanagement.Student.model.Module;
import com.roberto.studentmanagement.Student.service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
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

        //check if admin is logged in and is a valid admin
        if (!adminIsLoggedIn(httpSession)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Request : Admin must be logged in");
        }

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

        //check if admin is logged in and is a valid admin
        if (!adminIsLoggedIn(httpSession)){
            return null;
        }
        return adminService.getAllStudents();
    }

    @DeleteMapping("/deleteStudent")
    public ResponseEntity<String> deleteStudent(@RequestParam int studentID){

        //check if admin is logged in and is a valid admin
        if (!adminIsLoggedIn(httpSession)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Request : Admin must be logged in");
        }

        return adminService.deleteStudent(studentID);
    }

    @PostMapping("/verifyAdminCredentials")
    public ResponseEntity<?> verifyAdminCredentials(@Valid @RequestBody LoginRequest loginRequest) {

        //ensure user have up to 5 attempts
        Object attempts = httpSession.getAttribute("admin_verification_attempts");

        //if this is their first attempt then create a session with their amount of attempt
        if(attempts == null){
            httpSession.setAttribute("admin_verification_attempts", 5);
        }


        //if a user has 5 failed attempt there is a future time they will be allowed to make attempts
        Object futureTime =  httpSession.getAttribute("future_time");
        //check if user exceeded attempt count by checking if the current time the got all attempts wroong is set
        if (futureTime!=null){

            LocalDateTime futureTimeFromSession = (LocalDateTime) futureTime;
            LocalDateTime now = LocalDateTime.now();
            boolean hasPassed = now.isAfter(futureTimeFromSession);//check if user waited the entire 5 minutes

            //calculate remaining time
            Duration duration = Duration.between(LocalDateTime.now(), futureTimeFromSession);
            long minutesLeft = duration.toMinutes();

            System.out.println("Duration + " + duration.toSeconds());

            if(!hasPassed) {//if time has not passed tell the user how much time is left
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wait " + (minutesLeft +1 ) + " min to try again");
            }
            else{//reset attempts to 5
                httpSession.setAttribute("admin_verification_attempts", 5);
                //clear time sessions used at login screen for verification
                httpSession.removeAttribute("future_time");
            }
        }

        //check the database to ensure admin matches the username and password
        Admin verifiedAdmin = adminService.verifyAdminCredentials(loginRequest);


        if (verifiedAdmin != null) {
            // Set session attributes
            httpSession.setAttribute("adminFullName", verifiedAdmin.getFname() + " " + verifiedAdmin.getLname());
            httpSession.setAttribute("adminEmail", verifiedAdmin.getEmail());

            //clear sessions used at login screen for verification
            httpSession.removeAttribute("admin_verification_attempts");
            httpSession.removeAttribute("future_time");

            // Return redirect path to frontend
            return ResponseEntity.ok("/adminDashboard");
        }

        //retrieve the number of attempts a user has left
        int numOfAttempts = (int) httpSession.getAttribute("admin_verification_attempts");

        numOfAttempts--;//reduce the number of attempts they have left

        httpSession.setAttribute("admin_verification_attempts",numOfAttempts);

        if (numOfAttempts <=1){
            System.out.println("Attempts : "+numOfAttempts);
            LocalDateTime now = LocalDateTime.now();//get current time
            //set time 5 minutes from  the current time
            httpSession.setAttribute("future_time", now.plusMinutes(5));
        }

        // Return error if credentials invalid
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials "+ numOfAttempts +" attempts left.");
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


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession httpSession) {

        Object adminFullName = httpSession.getAttribute("adminFullName");
        Object adminEmail = httpSession.getAttribute("adminEmail");

        if (adminFullName ==null && adminEmail ==null){
            httpSession.invalidate(); // Invalidate the current user session
        }

        return ResponseEntity.ok("You have been logged out successfully.");
    }


    @PostMapping("/addModule")
    public ResponseEntity<?> addModule(@Valid @RequestBody Module module, HttpSession httpSession){

        //check if admin is logged in and is a valid admin
        if (!adminIsLoggedIn(httpSession)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Request : Admin must be logged in");
        }
        //retrieve admin email from session
        Object adminEmail  = httpSession.getAttribute("adminEmail");
        //check if a email was set
        if (adminEmail == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin Email is needed");
        }

        module.setAdded_by((String) adminEmail);//set added by using admin_email from session data

        return adminService.addModule(module);


    }


    public Boolean adminIsLoggedIn(HttpSession httpSession){
        //retrieve admin email from session
        Object adminEmail = httpSession.getAttribute("adminEmail");

        if(adminEmail==null){//if adminEmail cannot be retrieved from
           return false;
        }
        return adminService.adminEmailExist((String) adminEmail);
    }


}
