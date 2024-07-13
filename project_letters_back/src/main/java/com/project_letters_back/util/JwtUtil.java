package com.project_letters_back.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret; // 토큰을 서명(sign)하고 검정하는 데 사용되는 비밀 키

    private static final long EXPIRATION_TIME = 86400000; // 하루

    // UUID를 추출하는 메서드
    public String extractUuid(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 만료날짜 추출
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 주어진 claims 추출
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 토큰에서 모든 claims 추출
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // JWT 토큰 생성

    // UUID를 사용하여 토큰 생성
    public String generateToken(String uuid) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, uuid);
    }

    // 클레임과 주체를 기반으로 JWT 토큰 생성
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // 10시간 유효
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // 토큰의 유효성 검사

    // 토큰이 만료되었는지 확인
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // UUID를 검증하는 메서드
    public Boolean validateToken(String token, String uuid) {
        final String extractedUuid = extractUuid(token);
        return (extractedUuid.equals(uuid) && !isTokenExpired(token));
    }
}
