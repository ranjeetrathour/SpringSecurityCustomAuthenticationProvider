package com.example.controller;

import com.example.entity.Student;
import com.example.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student-info")
@Slf4j
public class StudentController {

    private final StudentService studentService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public StudentController(StudentService studentService, AuthenticationManager authenticationManager) {
        this.studentService = studentService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/save")
    public void saveUser(@RequestBody Student student) {
        try {
            studentService.saveStudent(student);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("User saved successfully");
    }

    @GetMapping("/findById/{id}")
    public Student findStudentById(@PathVariable("id") Long id) {
        return studentService.findStudentById(id);
    }

    @GetMapping("/findByEmail/{studentEmail}")
    public Student findStudentByEmail(@PathVariable("studentEmail") String studentEmail) {
        return studentService.findStudentByEmail(studentEmail);
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody Student student) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(student.getStudentEmail(), student.getStudentPassword())
            );
            return "Authentication successful for user: " + authentication.getName();
        } catch (AuthenticationException e) {
            return "Authentication failed: " + e.getMessage();
        }
    }
}
