package goormton.team.gotjob.global.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.auth")
public record JwtProperties(
        String tokenSecret,
        long accessTokenExpirationMsec,
        long refreshTokenExpirationMsec
) {}