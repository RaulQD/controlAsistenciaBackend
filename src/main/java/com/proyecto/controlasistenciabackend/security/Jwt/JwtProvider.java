package com.proyecto.controlasistenciabackend.security.Jwt;

import com.proyecto.controlasistenciabackend.security.entity.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Date;


@Component
public class JwtProvider{

    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.secret}")
    private String secreat_validation;
    @Value("${jwt.expiration}")
    private int expiration ;
    public String generateAccessToken(Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        logger.info("secret: " + secret);
        //TODO:Generar token
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(getSigningKey(secret))
                .claim("roles", userPrincipal.getAuthorities())
                .compact();
    }
    private Key getSigningKey(String secret){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    /* A method that returns a key for signing the token. */
    public String getUsuarioFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey(secret)).build().parseClaimsJws(token).getBody().getSubject();

    }
    public boolean validateToken(String token) {
        try{
            Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey(secreat_validation)).build().parseClaimsJws(token).getBody();
            String username = claims.getSubject();
            Date expiration = claims.getExpiration();
            //verifico si el token ha expirado
            if (expiration.before(new Date())) {
                return false;
            }
        }catch(MalformedJwtException e){
            logger.error("Token mal formado" + e.getMessage());
        }catch(UnsupportedJwtException e) {
            logger.error("Token no soportado" + e.getMessage());
        }catch(ExpiredJwtException e) {
            logger.error("Token expirado" + e.getMessage());
        }catch(IllegalArgumentException e) {
            logger.error("Token vacío" + e.getMessage());
        }catch(SignatureException e) {
            logger.error("Fallo en la firma" + e.getMessage());
        }catch(Exception e){
            logger.error("Error en el método validateToken" + e.getMessage());
        }
        return false;
    }

}
