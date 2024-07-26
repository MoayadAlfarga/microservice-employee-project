package com.security_service.security_service_employee.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtGenerateService {
        @Value("${secret-key}")
        private String secretKey;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Claims extractAllClaim(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigInKey()).build().
                  parseClaimsJws(token).getBody();
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        Claims claims = extractAllClaim(token);
        return claimsTFunction.apply(claims);


    }


    private Key getSigInKey() {
        byte[] KeyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(KeyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUserName(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpire(token));
    }

    public boolean isTokenExpire(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
//        return Jwts.builder().setClaims(extractClaims).setSubject(userDetails.getUsername())
//                  .setIssuedAt(new Date(System.currentTimeMillis()))
//                  .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
//                  .signWith(getSigInKey(), SignatureAlgorithm.HS256)
//                  .compact();
//    }
    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
        // Extract user roles from userDetails (assuming userDetails is an instance of UserDetails)
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = authorities.stream()
                  .map(GrantedAuthority::getAuthority)
                  .collect(Collectors.toList());
        extractClaims.put("roles", roles);

        return Jwts.builder()
                  .setClaims(extractClaims)
                  .setSubject(userDetails.getUsername())
                  .setIssuedAt(new Date(System.currentTimeMillis()))
                  .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                  .signWith(getSigInKey(), SignatureAlgorithm.HS256)
                  .compact();
    }
    public void validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(getSigInKey()).build().parseClaimsJws(token);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }


}
