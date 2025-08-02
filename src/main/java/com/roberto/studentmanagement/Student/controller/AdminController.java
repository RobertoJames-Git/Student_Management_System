package com.roberto.studentmanagement.Student.controller;

import com.roberto.studentmanagement.Student.model.Student;
import com.roberto.studentmanagement.Student.service.adminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {

    @Autowired
    private adminService adminService;

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


}
