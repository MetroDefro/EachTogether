package com.et.eachtogether.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration.access}")
    private Long EXPIRATION_ACCESS;
    @Value("${jwt.expiration.refresh}")
    private Long EXPIRATION_REFRESH;

    private final String TOKEN_PREFIX = "Bearer ";

    private final String HEADER_ACCESS = "Access";
    private final String HEADER_REFRESH = "Refresh";

    private Key key;

    @PostConstruct
    public void init() { // 생성자가 호출된 뒤 이 메서드가 실행된다.
        byte[] bytes = Base64.getDecoder().decode(SECRET_KEY); // 한 번 인코딩 되어있기 때문에 디코딩
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateAccessToken(Authentication authentication) {
        String username = authentication.getName();
        return createToken(username, createClaims(username, HEADER_ACCESS), EXPIRATION_ACCESS);
    }

    public String generateRefreshToken(Authentication authentication) {
        String username = authentication.getName();
        return createToken(username, createClaims(username, HEADER_REFRESH), EXPIRATION_REFRESH);
    }

    public String getAccessToken(HttpServletRequest request) {
        return getToken(request.getHeader(HEADER_ACCESS));
    }

    public String getRefreshToken(HttpServletRequest request) {
        return getToken(request.getHeader(HEADER_REFRESH));
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean isValidToken(String token) {
        try {
            if(!StringUtils.hasText(token)) {
                throw new JwtException("Token is empty");
            }
            getClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    private Map<String, Object> createClaims(String email, String headerRefresh) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("category", headerRefresh);
        claims.put("username", email);
        claims.put("role", "ROLE_USER");
        return claims;
    }

    private String createToken(final String email, Map<String, Object> claims, Long expiredMs) {
        Date curDate = new Date();
        Date expireDate = new Date(curDate.getTime() + expiredMs);

        return TOKEN_PREFIX + Jwts.builder()
                .setSubject(email)
                .setClaims(claims)
                .setExpiration(expireDate)
                .setIssuedAt(curDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String getToken(String token) {
        if(StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.replace(TOKEN_PREFIX, "");
        }
        return null;
    }
}
