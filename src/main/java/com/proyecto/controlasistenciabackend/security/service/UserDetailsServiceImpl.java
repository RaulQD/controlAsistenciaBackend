package com.proyecto.controlasistenciabackend.security.service;

import com.proyecto.controlasistenciabackend.entity.Usuario;
import com.proyecto.controlasistenciabackend.repository.UsuarioRepository;
import com.proyecto.controlasistenciabackend.security.entity.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario =usuarioRepository.findByUsuario(username);
        if(usuario == null){
            logger.error("Error en el login : no existe el usuario '" + username + "' en el sistema");
            throw new UsernameNotFoundException("Error en el login : no existe el usuario '" + username + "' en el sistema");
        }
        List<GrantedAuthority> authorities = usuario.getRoles().stream().map(
                rol -> new SimpleGrantedAuthority(rol.getRoles())).collect(Collectors.toList());
        return new UserPrincipal(usuario.getNombre(),usuario.getUsuario(), usuario.getContrasena(), authorities );
    }
}
