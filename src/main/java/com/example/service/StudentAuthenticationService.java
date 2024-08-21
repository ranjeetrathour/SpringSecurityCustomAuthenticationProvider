package com.example.service;

import com.example.entity.Role;
import com.example.entity.Student;
import com.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentAuthenticationService implements UserDetailsService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentAuthenticationService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findByStudentEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found with this email: " + username));

        String role = student.getRole().getRoleName();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        return User.withUsername(student.getStudentEmail())
                .password(student.getStudentPassword())
                .authorities(authorities)
                .build();
    }
}
