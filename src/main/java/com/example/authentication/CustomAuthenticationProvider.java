package com.example.authentication;

import com.example.entity.Student;
import com.example.repository.StudentRepository;
import com.example.service.StudentAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final StudentAuthenticationService studentAuthenticationService;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;

    @Autowired
    public CustomAuthenticationProvider(StudentAuthenticationService studentAuthenticationService, PasswordEncoder passwordEncoder, StudentRepository studentRepository) {
        this.studentAuthenticationService = studentAuthenticationService;
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Load UserDetails
        UserDetails userDetails = studentAuthenticationService.loadUserByUsername(username);

        // Check credentials
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Authentication failed: invalid credentials.");
        }

        // Fetch the Student entity for additional checks
        Optional<Student> student = studentRepository.findByStudentEmail(username);

        if (!student.isPresent()) {
            throw new BadCredentialsException("Authentication failed: student not found.");
        }

        if (student.get().getStudentAge() < 18) {
            throw new BadCredentialsException("Authentication failed: student is underage.");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
