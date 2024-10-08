package com.project.server.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TokenService {
    @Value("${app.token.authExpire}")
    private String expireTime;
    @Value("${app.key}")
    private String key;

    private Key getKey() {
        byte[] bytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String getUserName(String token) throws ExpiredJwtException, MalformedJwtException{
        return extractCl(token,Claims::getSubject);
    }

    public <T> T extractCl(String token, Function<Claims,T> resolver) throws ExpiredJwtException, MalformedJwtException {
        final Claims cl = extract(token);
        return resolver.apply(cl);
    }

    public Claims extract(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String genToken(
            Long userId, String username
    ) {
        HashMap<String,Object> claims = new HashMap<>();
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuer("http://localhost")
                .setAudience(userId.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+(Long.parseLong(expireTime)*1000)))
                .signWith(getKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validate(String token, UserDetails details) throws ExpiredJwtException, MalformedJwtException {
        final String name = getUserName(token);
        if (!name.equals(details.getUsername())) return false;
        return !isExpired(token);
    }

    public boolean invalidate(String token,UserDetails details) {
        final String name = getUserName(token);
        if (!name.equals(details.getUsername())) return false;
        final Claims cl = extract(token);
        cl.setExpiration(Date.from(Instant.now()));

        return true;
    }

    private boolean isExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    private Date getExpirationDate(String token) {
        return extractCl(token, Claims::getExpiration);
    }

}
