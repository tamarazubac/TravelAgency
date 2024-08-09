package com.example.TravelAgency.config.security.jwt;

import com.example.TravelAgency.config.security.SecurityConstants;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//@Component
//
//public class JwtTokenGenerator implements Serializable {
//
//    private static final long serialVersionUID = -2550185165626007488L;
//
//    public String generateToken(Authentication authentication){
//        String username=authentication.getName(); //get username
//
//        Date currentDate=new Date();
//        Date expireDate=new Date(currentDate.getTime()+ SecurityConstants.JWT_EXPIRATION * 1000);
//
//        String token= Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date()) //current date
//                .setExpiration(expireDate)
//                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
//                .compact();
//        return token;
//
//    }
//
//
//    public String getUsernameFromJWT(String token){
//        Claims claims=Jwts.parser()
//                .setSigningKey(SecurityConstants.JWT_SECRET)
//                .parseClaimsJws(token)
//                .getBody();
//        return claims.getSubject();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token);
//            return true;
//        } catch (ExpiredJwtException ex) {
//            throw new AuthenticationCredentialsNotFoundException("JWT has expired!", ex);
//        } catch (SignatureException ex) {
//            throw new AuthenticationCredentialsNotFoundException("JWT signature is invalid!", ex);
//        } catch (MalformedJwtException ex) {
//            throw new AuthenticationCredentialsNotFoundException("JWT is malformed!", ex);
//        } catch (JwtException ex) {
//            throw new AuthenticationCredentialsNotFoundException("JWT is invalid!", ex);
//        } catch (Exception ex) {
//            throw new AuthenticationCredentialsNotFoundException("An error occurred while validating JWT!", ex);
//        }
//    }
//}

@Component
public class JwtTokenGenerator implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public String generateToken(Authentication authentication){
        String username = authentication.getName(); // Get username

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION * 1000);

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date()) // current date
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
                .compact();
        return token;
    }

    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public List<String> getRolesFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("roles", List.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT has expired!", ex);
        } catch (SignatureException ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT signature is invalid!", ex);
        } catch (MalformedJwtException ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT is malformed!", ex);
        } catch (JwtException ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT is invalid!", ex);
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("An error occurred while validating JWT!", ex);
        }
    }
}

