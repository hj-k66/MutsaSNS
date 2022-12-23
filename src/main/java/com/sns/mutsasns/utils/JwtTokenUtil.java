package com.sns.mutsasns.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {
    public static String createToken(Long userId,String key,long expireTimeMs){
        Claims claims = Jwts.claims();
        claims.put("userId",userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) //토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256,key) //어떤 암호화방식으로 사인할지, key로 잠금
                .compact();
    }

    public static String getUserName(String token, String secretKey){
        return extractClaims(token, secretKey).get("userName",String.class);
    }

    public static boolean isExpired(String token, String secretKey){
        Date expirationDate = extractClaims(token, secretKey).getExpiration();
        return expirationDate.before(new Date());
    }

    private static Claims extractClaims(String token, String key){
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }
}
