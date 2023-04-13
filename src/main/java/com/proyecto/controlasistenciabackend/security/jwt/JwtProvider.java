package com.proyecto.controlasistenciabackend.security.jwt;

import com.proyecto.controlasistenciabackend.security.dto.UserPrincipal;
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

//AYUDA A CREAR LOS TOKENS Y VALIDARLOS
@Component
public class JwtProvider {
    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    //SECRET TOKEN
    @Value("${jwt.secret}")
    private String secret;
    //EXPIRATION TIME
    @Value("${jwt.expiration}")
    private int expiration;

    //GENERATE TOKEN CORRESPONDIENTE A UN USUARIO MÁS SU EXPIRACIÓN
    public String generateToken(Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        logger.info("secret: " + secret);
        //TODO: GENERATE TOKEN
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(getSecretKey(secret))
                .claim("roles", userPrincipal.getAuthorities())
                .compact();
    }
    //HASHEA EL TOKEN
    private Key getSecretKey(String secret){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    //OBTIENE EL USUARIO DEL TOKEN
    public String getUserNameFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(getSecretKey(secret)).build().parseClaimsJwt(token).getBody().getSubject();
    }
    //VALIDA EL TOKEN SI EXPIRO, NO ESTA SUPORTEAD OO ESTA MAL FORMADO
    public boolean validateToken(String token){
        try{
            Claims calims = Jwts.parserBuilder().setSigningKey(getSecretKey(secret)).build().parseClaimsJwt(token).getBody();
            String username = calims.getSubject();
            Date expirationDate = calims.getExpiration();
            if( expirationDate.before(new Date())) {
                return true;
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
