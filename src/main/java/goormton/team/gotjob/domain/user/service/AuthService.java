package goormton.team.gotjob.domain.user.service;


import goormton.team.gotjob.domain.user.dto.*;
import goormton.team.gotjob.domain.user.domain.*;
import goormton.team.gotjob.domain.user.repository.*;
import goormton.team.gotjob.domain.common.ApiException;
import goormton.team.gotjob.global.security.JwtProperties;
import goormton.team.gotjob.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository users;
    private final UserProfileRepository profiles;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider tokens;
    private final JwtProperties jwtProps;

    @Transactional
    public SignupResponse signup(SignupRequest req){
        if (users.existsByUsername(req.username())) throw new ApiException(409,"username exists");
        if (users.existsByEmail(req.email())) throw new ApiException(409,"email exists");

        var u = User.builder()
                .username(req.username())
                .password(encoder.encode(req.password()))
                .email(req.email())
                .realName(req.realName())
                .phone(req.phone())
                .role(Role.valueOf(req.role()))
                .userStatus(UserStatus.ACTIVE) // 주의: 엔티티 필드명(userStatus)
                .build();
        users.save(u);

        var p = UserProfile.builder().user(u).build();
        u.attachProfile(p);
        profiles.save(p);

        return new SignupResponse(u.getId(), u.getUsername(), u.getEmail(), u.getRole().name());
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest req){
        var u = users.findByUsername(req.username())
                .orElseThrow(() -> new ApiException(404,"user not found"));
        if (!encoder.matches(req.password(), u.getPassword()))
            throw new ApiException(401,"bad credentials");

        String access = tokens.createAccessToken(u.getId(), u.getUsername(), u.getRole().name());
        long expiresInSec = jwtProps.accessTokenExpirationMsec() / 1000L;

        return new TokenResponse(access, "Bearer", expiresInSec);
    }
}