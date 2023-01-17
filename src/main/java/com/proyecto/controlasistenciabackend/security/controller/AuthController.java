package com.proyecto.controlasistenciabackend.security.controller;

import com.proyecto.controlasistenciabackend.security.Jwt.JwtProvider;
import com.proyecto.controlasistenciabackend.security.entity.JwtDto;
import com.proyecto.controlasistenciabackend.security.entity.LoginUsuario;
import com.proyecto.controlasistenciabackend.security.entity.UserPrincipal;
import com.proyecto.controlasistenciabackend.service.UsuarioService;
import com.proyecto.controlasistenciabackend.util.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class AuthController {

    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody LoginUsuario loginUsuario) {
        logger.info("Iniciando el método login" + loginUsuario.getUsuario());
        logger.info("Iniciando el metodo contraseña" + loginUsuario.getContrasena());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getUsuario(), loginUsuario.getContrasena()));
        logger.info("Iniciando el método authentication" + authentication);
        logger.info("Inicio de generación de token");

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateAccessToken(authentication);
        logger.info("Fin de generación de token" + jwt);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        logger.info("Fin de generación de token" + userPrincipal.toString());
        JwtDto jwtDto = new JwtDto(jwt, userPrincipal.getUsername(), userPrincipal.getNombre(), userPrincipal.getAuthorities());

        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
    }
}
