package com.proyecto.controlasistenciabackend.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Decoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "123";
        String encodedPassword = "$2a$10$Ii4GcDgXMI8OnCJrK7kVrOh/5b0llHg3xOq.YPEB9d7x6spvYsZOq";

        boolean isPasswordMatch = passwordEncoder.matches(password, encodedPassword);
        System.out.println("Password : " + password + "   isPasswordMatch    : " + isPasswordMatch);

    }
}
