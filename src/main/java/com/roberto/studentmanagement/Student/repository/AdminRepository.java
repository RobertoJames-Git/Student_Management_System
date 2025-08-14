package com.roberto.studentmanagement.Student.repository;

import com.roberto.studentmanagement.Student.model.Admin;
import com.roberto.studentmanagement.Student.model.Module;
import com.roberto.studentmanagement.Student.model.Student;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.swing.tree.RowMapper;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AdminRepository {

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

    public Boolean studentEmailExist(String stdEmail) {

        String sql = "SELECT COUNT(*) FROM student WHERE email = ?";
        //query database for the amount of persons in the database that already has that email
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class, stdEmail);

        //if no records exist then return false
        return count != null && count != 0;
    }


    public ResponseEntity<String> deleteStudent(int studentID) {
        try {
            String sql = "Delete from student where studentID = ?";
            int rowsAffected = jdbcTemplate.update(sql, studentID);
            if (rowsAffected == 0){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student does not exist");
            }
        } catch (DataAccessException e) {
            System.out.println("Data Access Error in deleteStudent : " +e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete student");
        }
        return ResponseEntity.ok("Successfully Removed student");
    }

    public Admin getAdminCredentials(String adminEmail) {

        try {

            //query the database to find an admin with the email
            String sql = "Select * from admin where email = ?";
            return  jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Admin.class), adminEmail);
        }
        catch (EmptyResultDataAccessException e){
            System.err.println("No admin was found");
            return null;//An admin with the email was not found
        }
        catch (DataAccessException e){
            System.out.println("Data Access Exception in verifyAdminCredentials: " + e.getMessage());
            return null;
        }
    }

    public Boolean addAdminToDatabase(@Valid Admin admin) {
        Integer rowsAffected=null;
        try {
            String sql = "Insert into admin(email,fname,lname,password) values(?,?,?,?)";
            //adds admin to database and return the number of ros affected
            rowsAffected = jdbcTemplate.update(sql, admin.getEmail(), admin.getFname(), admin.getLname(), admin.getPassword());

            /*if a row was affected it means that the data was added successfully
            so true will be return else false is returned
            */
            if (rowsAffected == 0) {
                return false;
            }
        }catch (DataAccessException e){
            System.out.println("Data access exception in addAdminToDatabase : " + e.getMessage());
            return false;
        }

        return true;
    }

    public boolean adminEmailExist(String adminEmail) {
        String sql = "Select Count(*) from admin where email = ? ";
        Integer emailCount=0;
        emailCount = jdbcTemplate.queryForObject(sql, Integer.class,adminEmail);

        //check if email was found after the database was queried
        if(emailCount==null|| emailCount == 0){
            return false;//email was not found
        }

        return true;//email was found in database
    }


    public ResponseEntity<String> addModuleToDatabase(Module module) {


        try {
            String sql = "insert into module (moduleCode, moduleName, credits, addedBy) values (?, ?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(sql, module.getModuleCode(), module.getModuleName(), module.getCredits(), module.getAdded_by());

            if (rowsAffected == 0) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to add module");
            }
        } catch (DuplicateKeyException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Module with code " + module.getModuleCode() + " already exists");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Module added successfully");
    }


}
