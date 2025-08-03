package com.roberto.studentmanagement.Student.service;


import com.roberto.studentmanagement.Student.model.Admin;
import com.roberto.studentmanagement.Student.model.Student;
import com.roberto.studentmanagement.Student.repository.AdminRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {


    private final AdminRepository adminRepository;
    private final PasswordService passwordService;

    public AdminService(AdminRepository adminRepository, PasswordService passwordService){
        this.adminRepository = adminRepository;
        this.passwordService = passwordService;
    }


    public Map<Boolean,String> addStudent(Student student) {

        Map<Boolean,String> queryResponse = new HashMap<Boolean, String>();

        //check if student is 18 year older
        if(calculateAge(student.getDob())<18){
            queryResponse.put(false,"Must be 18 or older");
            return queryResponse;
        }

        String password = student.getPassword();

        // Validate that the password is at least 8 characters and contains both letters and digits only
        if (password.length() < 8 || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$")) {
            queryResponse.put(false, "Must be at least 8 characters long and alphanumeric (contain both letters and digits)");
            return queryResponse;
        }

        //encrypt the users password
        password = passwordService.encrypt(password);
        student.setPassword(password);//change plain text password to encrypted password


        //check if student email already exist
        if (adminRepository.emailExist(student.getEmail())){
            queryResponse.put(false,"Use another email");
            return queryResponse;
        }

        //add student to the database
        return adminRepository.addStudent(student);
    }



    public int calculateAge(String dobString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(dobString, formatter);
        LocalDate today = LocalDate.now();
        //return the amount of years between the dob of the student and the current year
        return Period.between(dob, today).getYears();
    }


    public List<Student> getAllStudents() {
        return adminRepository.getAllStudents();
    }

    public ResponseEntity<String> deleteStudent(int studentID) {
        return adminRepository.deleteStudent(studentID);
    }

    public ResponseEntity<String> verifyAdminCredentials(Admin admin) {
        //get admin details from database that corresponds to the ID
        Admin adminFromDB= adminRepository.verifyAdminCredentials(admin);
        String errorMsg="Invalid AdminID/Password";

        //check if a admin was returned or if the hashed password in the database corresponds to what the user entered
        if(adminFromDB == null|| !passwordService.compare(admin.getPassword(), adminFromDB.getPassword()) ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMsg);
        }

        return ResponseEntity.ok("Login Successful");
    }
}
