package com.proyecto.controlasistenciabackend.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "S0p0rt3123";
        String encodedPassword = encoder.encode(password);
        System.out.println();
        System.out.println("Password: " + password);
        System.out.println("Encoded Password is : " + encodedPassword);
    }

}
