package com.proyecto.controlasistenciabackend.security.controller;


import com.proyecto.controlasistenciabackend.security.dto.JwtDto;
import com.proyecto.controlasistenciabackend.security.dto.LoginUsuario;
import com.proyecto.controlasistenciabackend.security.dto.UserPrincipal;
import com.proyecto.controlasistenciabackend.security.jwt.JwtProvider;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class AuthController {

    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/signin")
    public ResponseEntity<JwtDto> loginUsuario(LoginUsuario loginUsuario){
        logger.info("Iniciando sesión del usuario: " + loginUsuario.getLogin());
        logger.info("Contraseña: " + loginUsuario.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getLogin(), loginUsuario.getPassword()));
        logger.info("Iniciando el metodo de autenticación: " + authentication );
        logger.info("Inicio de generación token");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        logger.info("Token generado: " + jwt);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        logger.info("Fin de generación de token" + userPrincipal.toString());
        JwtDto jwtDto = new JwtDto(jwt,userPrincipal.getNombre(),userPrincipal.getUsername(),userPrincipal.getAuthorities());
        return new ResponseEntity<JwtDto>(jwtDto, HttpStatus.OK);
    }

}
