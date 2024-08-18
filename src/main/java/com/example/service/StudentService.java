package com.example.service;

import com.example.entity.Role;
import com.example.entity.Student;
import com.example.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private  StudentRepository studentRepository;
    private  PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveStudent(Student student) throws Exception {
        Optional<Student> studentExist = studentRepository.findByStudentEmail(student.getStudentEmail());
        if (studentExist.isPresent()){
            throw new Exception("akjska");
        }
        String encodePassword = passwordEncoder.encode(student.getStudentPassword());
        student.setStudentPassword(encodePassword);
        studentRepository.save(student);
         log.info("user success saved");
    }

    public Student findStudentById(Long id){
      Student st = studentRepository.findById(id).orElse(null);
      return st;
    }

    public Student findStudentByEmail(String studentEmail){
        Student st = studentRepository.findByStudentEmail(studentEmail).orElse(null);
        return st;
    }
}
