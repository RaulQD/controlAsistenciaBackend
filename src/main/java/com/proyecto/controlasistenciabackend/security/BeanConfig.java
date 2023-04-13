package com.proyecto.controlasistenciabackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//CLASE DE CONFIGURACIÓN PARA INYECTAR LOS BEANS DE SPRING SECURITY
@Configuration
public class BeanConfig {

    //MÉTODO QUE INYECTA EL PASSWORD ENCODER PARA PODER USARLO EN EL CONTROLADOR DE USUARIOS
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //MÉTODO QUE INYECTA EL AUTHENTICATION MANAGER PARA PODER USARLO EN EL CONTROLADOR DE USUARIOS
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
