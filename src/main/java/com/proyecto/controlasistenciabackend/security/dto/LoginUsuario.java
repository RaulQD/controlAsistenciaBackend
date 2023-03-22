package com.proyecto.controlasistenciabackend.security.dto;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;



public class LoginUsuario {
    private String username;
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
