package com.roberto.studentmanagement.Student.service;


import com.roberto.studentmanagement.Student.model.Student;
import com.roberto.studentmanagement.Student.repository.adminRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class adminService {


    private final adminRepository adminRepository;

    public adminService(adminRepository adminRepository){
        this.adminRepository = adminRepository;
    }


    public Map<Boolean,String> addStudent(Student student) {

        Map<Boolean,String> queryResponse = new HashMap<Boolean, String>();

        //check if student is 18 year older
        if(calculateAge(student.getDob())<18){
            queryResponse.put(false,"Must be 18 years old");
            return queryResponse;
        }

        String password = student.getPassword();

        // Validate that the password is at least 8 characters and contains both letters and digits only
        if (password.length() < 8 || !password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$")) {
            queryResponse.put(false, "Must be at least 8 characters long and alphanumeric (contain both letters and digits)");
            return queryResponse;
        }

        //check if student email already exist
        if (adminRepository.emailExist(student.getEmail())){
            queryResponse.put(false,"Use another email");
            return queryResponse;
        }

        //add student to the database
        return addStudent(student);
    }



    public int calculateAge(String dobString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(dobString, formatter);
        LocalDate today = LocalDate.now();
        //return the the amount of years between the dob of the student and the current year
        return Period.between(dob, today).getYears();
    }


    public List<Student> getAllStudents() {

        return adminRepository.getAllStudents();

    }
}
