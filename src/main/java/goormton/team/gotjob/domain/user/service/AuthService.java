package goormton.team.gotjob.domain.user.service;


import goormton.team.gotjob.domain.user.dto.*;
import goormton.team.gotjob.domain.user.domain.*;
import goormton.team.gotjob.domain.user.repository.*;
import goormton.team.gotjob.domain.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository users;
    private final UserProfileRepository profiles;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Transactional
    public SignupResponse signup(SignupRequest req){
        if(users.existsByUsername(req.username())) throw new ApiException(409,"username exists");
        if(users.existsByEmail(req.email())) throw new ApiException(409,"email exists");

        var u = User.builder()
                .username(req.username()).password(encoder.encode(req.password()))
                .email(req.email()).realName(req.realName()).phone(req.phone())
                .role(Role.valueOf(req.role())).status(UserStatus.ACTIVE).build();
        users.save(u);

        var p = UserProfile.builder().user(u).build();
        u.attachProfile(p); profiles.save(p);

        return new SignupResponse(u.getId(), u.getUsername(), u.getEmail(), u.getRole().name());
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest req){
        var u = users.findByUsername(req.username()).orElseThrow(()->new ApiException(404,"user not found"));
        if(!encoder.matches(req.password(), u.getPassword())) throw new ApiException(401,"bad credentials");
        return new TokenResponse("dummy-jwt-token","Bearer",3600);
    }
}