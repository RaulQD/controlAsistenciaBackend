package com.proyecto.controlasistenciabackend.security.Jwt;

import com.proyecto.controlasistenciabackend.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final static Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserDetailsServiceImpl userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info(">>> Ingreso doFilterInternal");
        try{
            String token = getToken(request);
            logger.info(">>> Llegó token ==> " + token);
            logger.info(">>> doFilterInternal >> token >> "  + token);
            if(token != null && jwtProvider.validateToken(token)){
                String usuario = jwtProvider.getUsuarioFromToken(token);
                logger.info(">>> doFilterInternal >> Usuario >> "  + usuario);
                UserDetails userDetails = userDetailsService.loadUserByUsername(usuario);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }catch(UsernameNotFoundException e){

            logger.error("Error en el método doFilterInternal" + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
    private String getToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            return header.replace("Bearer ", " ");
        }
        return null;
    }

}
