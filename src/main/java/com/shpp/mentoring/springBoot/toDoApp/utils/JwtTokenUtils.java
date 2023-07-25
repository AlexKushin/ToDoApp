package com.shpp.mentoring.springBoot.toDoApp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.lifetime}")
    private Duration jwyLifetime;
//по данным пользователя формирует токен
    //этот метод нужен для того чтоб сгенерировать токен и вернуть его клиенту, когда нам пришлют логин и пароль
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", rolesList);

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwyLifetime.toMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
               .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    //получить имя из токена
    public String getUsername(String token){
        return getAllClaimsFromToken(token).getSubject();
    }
    //получить роли из токена
    public List<String> getRoles(String token){
        return getAllClaimsFromToken(token).get("roles", List.class);
    }
    //когда нам нужно достать данные из токена мы использем данный метод,
    //если кто то захочет подменить данные и  secret не совпадет мы получим ошибку
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                //.parseClaimsJwt(token)
                .parseClaimsJws(token)
                .getBody();

    }
}
