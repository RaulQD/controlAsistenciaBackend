package com.proyecto.controlasistenciabackend.security.service;

import com.proyecto.controlasistenciabackend.entity.Usuario;
import com.proyecto.controlasistenciabackend.repository.UsuarioRepository;
import com.proyecto.controlasistenciabackend.security.UserPrincipal;
import com.proyecto.controlasistenciabackend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UsuarioService usuarioService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorDni(username).get();
        return UserPrincipal.build(usuario);
    }
}
