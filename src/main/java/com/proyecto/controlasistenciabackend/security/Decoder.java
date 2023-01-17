package com.proyecto.controlasistenciabackend.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Decoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "S0p0rt3123";
        String encodedPassword = "$2a$10$tDddjvQSQbRUUMYvVs7TNeO7Fc3IRv/bwxLA6vzQpJ2.HuMz/sfyO";

        boolean isPasswordMatch = passwordEncoder.matches(password, encodedPassword);
        System.out.println("Password : " + password + "   isPasswordMatch    : " + isPasswordMatch);

    }
}
