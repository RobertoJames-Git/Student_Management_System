package com.roberto.studentmanagement.Student.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final PasswordEncoder encoder;

    public PasswordService() {
        this.encoder = new BCryptPasswordEncoder();
    }

    public String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean compare(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}


