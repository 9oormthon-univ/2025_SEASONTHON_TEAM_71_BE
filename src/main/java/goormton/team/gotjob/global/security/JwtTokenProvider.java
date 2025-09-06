package goormton.team.gotjob.global.security;

import goormton.team.gotjob.global.error.DefaultException;
import goormton.team.gotjob.global.payload.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private final long accessTtl;
    private final long refreshTtl;

    public JwtTokenProvider(JwtProperties props) {
        this.key = Keys.hmacShaKeyFor(props.tokenSecret().getBytes());
        this.accessTtl = props.accessTokenExpirationMsec();
        this.refreshTtl = props.refreshTokenExpirationMsec();
    }

    public String createAccessToken(Long userId, String username, String role) {
        return build(userId, username, role, accessTtl);
    }

    public String createRefreshToken(Long userId, String username, String role) {
        return build(userId, username, role, refreshTtl);
    }


    private String build(Long userId, String username, String role, long ttl) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .addClaims(Map.of("username", username, "role", role))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(ttl)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public Jws<Claims> parse(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public Long getUserId(String token){
        return Long.valueOf(parse(token).getBody().getSubject());
    }

    public String resolveToken(String token){
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        validateToken(jwt);
        return jwt;
    }

    public boolean validateToken(String token) {
        try {
            parse(token);
            return true;
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            throw new DefaultException(ErrorCode.INVALID_JWT_ERROR);
        } catch (ExpiredJwtException e) {
            throw new DefaultException(ErrorCode.JWT_EXPIRED_ERROR);
        } catch (UnsupportedJwtException e) {
            throw new DefaultException(ErrorCode.UNSUPPORTED_JWT_ERROR);
        }
    }

    public Boolean isExpired(String token) {
        try {
            return parse(token).getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            // 파싱 과정에서 만료 예외가 터지면 당연히 만료된 것
            return true;
        } catch (Exception e) {
            // 다른 종류의 예외가 발생하면 유효하지 않은 토큰이지만, '만료'된 것은 아닐 수 있음
            // 이 경우 정책에 따라 false나 예외를 반환할 수 있음
            return false;
        }
    }
}