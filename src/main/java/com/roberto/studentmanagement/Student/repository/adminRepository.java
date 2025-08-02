package com.roberto.studentmanagement.Student.repository;

import com.roberto.studentmanagement.Student.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class adminRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Student> getAllStudents() {
        String sql = "SELECT studentID, fname, lname, dob, email FROM student";
        //returns a list of all students from the database
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Student.class));
    }

    public Map<Boolean,String>  addStudent(Student student){
        Map<Boolean,String> queryResponse  = new HashMap<>();
        String sql = "INSERT INTO student (fname, lname,dob,email,password) VALUES (?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, student.getFname(), student.getLname(), student.getDob(), student.getEmail(), student.getPassword());

        } catch (DataAccessException e) {
            System.out.println("Data access exception -> "  + e.getMessage());
            queryResponse.put(false,"Failed to add student");
            return queryResponse;
        }

        queryResponse.put(true,"Add Student Successfully");
        return queryResponse;
    }

    public Boolean emailExist(String stdEmail) {

        String sql = "SELECT COUNT(*) FROM student WHERE email = ?";
        //query database for the amount of persons in the database that already has that email
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class, stdEmail);

        //if no records exist then return false
        if( count==null|| count ==0){
            return  false;
        }
        return true;
    }




}
