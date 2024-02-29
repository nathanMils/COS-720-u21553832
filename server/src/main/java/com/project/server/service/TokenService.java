package com.project.server.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class TokenService {

    @Value("${app.token.expire.factor}")
    private String expireFactor;
    @Value("${app.key}")
    private String key;

    private Key getKey() {
        byte[] bytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String getUserName(String token) {
        return extractCl(token,Claims::getSubject);
    }

    public <T> T extractCl(String token, Function<Claims,T> resolver) {
        final Claims cl = extract(token);
        return resolver.apply(cl);
    }

    public Claims extract(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey())
                .build().parseClaimsJws(token)
                .getBody();
    }

    public String genToken(
            UserDetails details
    ) {
        HashMap<String,Object> Claims = new HashMap<>();
        return Jwts
                .builder()
                .setClaims(Claims)
                .setSubject(details.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L *60 *Integer.parseInt(expireFactor)))
                .signWith(getKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validate(String token, UserDetails details) {
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
