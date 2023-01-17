package com.proyecto.controlasistenciabackend.security.entity;

import lombok.ToString;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@ToString
public class JwtDto {
    private String token;
    private String bearer = "Bearer ";
    private String nombre;
    private String usuario;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtDto(String token, String usuario, String nombre, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.usuario = usuario;
        this.nombre = nombre;
        this.authorities = authorities;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getBearer() {
        return bearer;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
