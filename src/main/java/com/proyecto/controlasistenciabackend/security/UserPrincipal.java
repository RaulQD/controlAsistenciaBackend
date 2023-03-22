package com.proyecto.controlasistenciabackend.security;

import com.proyecto.controlasistenciabackend.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private String nombre;
    private String dni;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(String nombre, String dni, String password, Collection<? extends GrantedAuthority> authorities) {
        this.nombre = nombre;
        this.dni = dni;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrincipal build(Usuario usuario){
        List<GrantedAuthority> authorities = usuario.getRoles().stream().map(
                role -> new SimpleGrantedAuthority(role.getNombre_rol().name())).collect(Collectors.toList());
        return new UserPrincipal(usuario.getNombre(), usuario.getDni(), usuario.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return dni;
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
}
