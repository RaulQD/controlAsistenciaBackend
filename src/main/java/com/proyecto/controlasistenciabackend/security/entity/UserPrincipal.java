package com.proyecto.controlasistenciabackend.security.entity;

import com.proyecto.controlasistenciabackend.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;



public class UserPrincipal implements UserDetails {

    private String nombre;
    private String usuario;
    private String contrasena;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(String nombre, String usuario, String contrasena, Collection<? extends GrantedAuthority> authorities) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.authorities = authorities;
    }

//    public static UserPrincipal build(Usuario usuario){
//        List<GrantedAuthority> authorities = usuario.getRoles().stream().map(
//                rol -> new SimpleGrantedAuthority(rol.getRoles())).collect(Collectors.toList());
//        return new UserPrincipal( usuario.getNombre(), usuario.getUsuario(), usuario.getContrasena(), authorities);
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return usuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
