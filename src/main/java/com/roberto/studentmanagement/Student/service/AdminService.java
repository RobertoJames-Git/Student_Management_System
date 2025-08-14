package com.roberto.studentmanagement.Student.service;


import com.roberto.studentmanagement.Student.model.Admin;
import com.roberto.studentmanagement.Student.model.LoginRequest;
import com.roberto.studentmanagement.Student.model.Student;
import com.roberto.studentmanagement.Student.model.Module;
import com.roberto.studentmanagement.Student.repository.AdminRepository;
import jakarta.servlet.http.HttpSession;
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
        if (adminRepository.studentEmailExist(student.getEmail())){
            queryResponse.put(false,"Use another email");
            return queryResponse;
        }

        //add student to the database
        return adminRepository.addStudent(student);
    }



    public int calculateAge(String dobString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(dobString, formatter);
        LocalDate today = LocalDate.now();//get todays date
        //return the amount of years between the dob of the student and the current year
        return Period.between(dob, today).getYears();
    }


    public List<Student> getAllStudents() {
        return adminRepository.getAllStudents();
    }

    public ResponseEntity<String> deleteStudent(int studentID) {
        return adminRepository.deleteStudent(studentID);
    }

    public Admin verifyAdminCredentials(LoginRequest loginRequest) {
        //get admin details from database that corresponds to the email
        Admin adminFromDB= adminRepository.getAdminCredentials(loginRequest.getEmail());

        //check if admin was returned or if the hashed password in the database corresponds to what the user entered
        if(adminFromDB == null|| !passwordService.compare(loginRequest.getPassword(), adminFromDB.getPassword()) ){
            return null;
        }

        return adminFromDB;
    }

    public Map<Boolean,String> addAdmin(Admin admin) {

        Map<Boolean, String> response = new HashMap<>();
        String password = admin.getPassword();

        // Validate that the password is at least 8 characters and contains both letters and digits only
        if (password.length() < 8 || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$")) {
            response.put(false, "Must be at least 8 characters long and alphanumeric (contain both letters and digits)");
            return response;
        }

        password = passwordService.encrypt(password);
        admin.setPassword(password);

        if(!adminRepository.addAdminToDatabase(admin)){
            response.put(false,"Failed to add admin");
        }
        else{
            response.put(true,"Admin added successfully");
        }
        return response;
    }

    public boolean adminEmailExist(String adminEmail) {
        return adminRepository.adminEmailExist(adminEmail);
    }

    public ResponseEntity<String> addModule(Module module) {
        return adminRepository.addModuleToDatabase(module);

    }

}
