package com.study.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JwtUtil {

    public  static String getUserName(String token, String secret) {

        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody().get("userName", String.class);
    }

    public static boolean isExpired(String token, String secret) {

        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());
    }
    public static String createToken(String userName, String secret, Long expireTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("userName", userName);

        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}

