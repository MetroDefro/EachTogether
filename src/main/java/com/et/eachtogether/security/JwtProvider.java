package com.et.eachtogether.security;

import com.et.eachtogether.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration.access}")
    private Long EXPIRATION_ACCESS;

    public final String HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    public static final String CLAIM_NICKNAME = "nickname";
    public static final String CLAIM_ROLE ="role";

    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(SECRET_KEY);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(User user) {
        Date curDate = new Date();
        Date expireDate = new Date(curDate.getTime() + EXPIRATION_ACCESS);

        return TOKEN_PREFIX + Jwts.builder()
            .setSubject(user.getEmail())
            .setClaims(createClaims(user))
            .setExpiration(expireDate)
            .setIssuedAt(curDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_NICKNAME, user.getNickname());
        // to do: 이후 관리자/유저 유연하게 넣을 수 있도록 변경 예정
        claims.put(CLAIM_ROLE, "ROLE_USER");
        return claims;
    }

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER);
        if(StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.replace(TOKEN_PREFIX, "");
        }
        return null;
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // to do: 직접 String을 작성하지 않는 방법은?
    public boolean isValidToken(String token, HttpServletRequest request) {
        try {
            getClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException | io.jsonwebtoken.security.SignatureException e) {
            request.setAttribute("error", "Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            request.setAttribute("error", "Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            request.setAttribute("error", "Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }

        return false;
    }
}
