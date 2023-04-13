package com.proyecto.controlasistenciabackend.security.dto;

import com.proyecto.controlasistenciabackend.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {
    private String nombre;
    private String login;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(String nombre, String login, String password, Collection<? extends GrantedAuthority> authorities) {
        this.nombre = nombre;
        this.login = login;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrincipal build(Usuario usuario){
        List<GrantedAuthority> authorities = usuario.getRoles().stream().map(
                role -> new SimpleGrantedAuthority(role.getRolNombre().name())).collect(Collectors.toList());
        return new UserPrincipal(usuario.getNombre(), usuario.getUsuario(), usuario.getPassword(), authorities);
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
        return login;
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
