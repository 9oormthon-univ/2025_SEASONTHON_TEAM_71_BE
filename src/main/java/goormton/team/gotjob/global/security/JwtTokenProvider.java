package goormton.team.gotjob.global.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

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
//    public String getRole(String token){
//        Object r = parse(token).getBody().get("role");
//        return r==null? null : r.toString();
//    }
//    public boolean isExpired(String token){
//        Date exp = parse(token).getBody().getExpiration();
//        return exp.before(new Date());
//    }
}