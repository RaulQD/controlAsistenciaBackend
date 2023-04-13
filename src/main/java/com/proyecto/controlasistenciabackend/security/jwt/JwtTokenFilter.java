package com.proyecto.controlasistenciabackend.security.jwt;

import com.proyecto.controlasistenciabackend.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//FILTRO PARA VALIDAR EL TOKEN EN CADA PETICIÓN
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final static Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    //MÉTODO QUE SE EJECUTA CADA VEZ QUE SE HACE UNA PETICIÓN DE UN USUARIO AUTENTICADO
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("Iniciando el metodo doFilterInternal");
        try{
            //OBTENEMOS EL TOKEN DEL HEADER DE LA PETICIÓN HTTP
            String token = getJwtFromRequest(request);
            //VALIDAMOS EL TOKEN
            if(token != null && jwtProvider.validateToken(token)){
                //OBTENEMOS EL USUARIO DEL TOKEN
                String username = jwtProvider.getUserNameFromToken(token);
                //OBTENEMOS LOS DETALLES DEL USUARIO
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //CREAMOS EL OBJETO DE AUTENTICACIÓN
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //ASIGNAMOS EL OBJETO DE AUTENTICACIÓN AL CONTEXTO DE SEGURIDAD
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch(Exception e){
            logger.error("Fallo en el metodo doFilterInternal" + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
    //MÉTODO QUE OBTIENE EL TOKEN DEL HEADER DE LA PETICIÓN HTTP
    private String getJwtFromRequest(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            return header.replace("Bearer ", "");
        }
        return null;
    }
}
