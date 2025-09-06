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
        // 1) role 파싱
        Role role = Role.valueOf(req.role().toUpperCase()); // (Role.from(...) 쓰고 있다면 그거 유지)

        // 2) 이메일 결정: 회사는 입력 없으면 더미 생성
        String email = req.email();
        if (role == Role.COMPANY && (email == null || email.isBlank())) {
            // username 은 unique 이므로, 이 값으로 유니크 보장되는 더미 메일 생성
            email = "corp_" + req.username() + "@gotjob.local";
        }

        // 3) 중복 체크 (회사도 더미메일 기준으로 유니크 유지)
        if(users.existsByUsername(req.username())) throw new ApiException(409,"username exists");
        if(users.existsByEmail(email))           throw new ApiException(409,"email exists");

        // 4) 유저 생성/저장
        var u = User.builder()
                .username(req.username())
                .password(encoder.encode(req.password()))
                .email(email)                         // ← 절대 null 아님
                .realName(req.realName())
                .phone(req.phone())                   // 회사면 null 가능
                .role(role)                           // PERSONAL or COMPANY
                .userStatus(UserStatus.ACTIVE)
                .build();
        users.save(u);

        var p = UserProfile.builder().user(u).build();
        u.attachProfile(p); profiles.save(p);

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