package com.proyecto.controlasistenciabackend.security;

import com.proyecto.controlasistenciabackend.security.Jwt.JwtEntryPoint;
import com.proyecto.controlasistenciabackend.security.Jwt.JwtTokenFilter;
import com.proyecto.controlasistenciabackend.security.Jwt.JwtProvider;
import com.proyecto.controlasistenciabackend.security.service.UserDetailsServiceImpl;
import jakarta.servlet.Filter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenFilter jwtTokenFilter;

    @Autowired
    private JwtEntryPoint jwtEntryPoint;

    AuthenticationManager authenticationManager;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder);
        authenticationManager = builder.build();
        http.authenticationManager(authenticationManager);
        http.cors().and().csrf().disable();
        http.authorizeHttpRequests().requestMatchers("/api/**").permitAll().anyRequest().authenticated();
        http.exceptionHandling().authenticationEntryPoint(jwtEntryPoint);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

