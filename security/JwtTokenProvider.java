package com.ndirituedwin.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public  String generateToken(Authentication authentication){
        log.info("logging the authenticatuion befor token generation {}",authentication);
        UserPrincipal principal= (UserPrincipal) authentication.getPrincipal();
        log.info("logging the principal {}",principal);

        Date  expirydate=new Date(new Date().getTime()+jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(Long.toString(principal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expirydate)
                .signWith(SignatureAlgorithm.HS256,jwtSecret)
                .compact();
    }

    public Long getUserIdFromJwt(String token){
        Claims claims=Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }
    public int getJwtExpirationInMillis(){
        return  jwtExpirationInMs;
    }


    public boolean validateToken(String authtoken){
        try {
                Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authtoken);
                return true;
        }catch (SignatureException exception){
            log.error("Invalid jwt Token signature {}",exception.getMessage());
        }catch (MalformedJwtException exceptio){
        log.error("Invalid jwt Token  {}",exceptio.getMessage());
       }catch (ExpiredJwtException ex){
            log.error("Expired Jwt token {}",ex.getMessage());
        }catch (UnsupportedJwtException un){
            log.error("Unsupported Jwt exception {}",un.getMessage());
        }catch (IllegalArgumentException ix){
            log.error("Jwt claims string is empty {}",ix.getMessage());
        }
        return  false;

    }

}
