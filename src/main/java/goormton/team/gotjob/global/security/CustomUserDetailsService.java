package goormton.team.gotjob.global.security;

import goormton.team.gotjob.domain.user.domain.User;
import goormton.team.gotjob.domain.user.repository.UserRepository;
import goormton.team.gotjob.domain.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository users;

    public CustomUserDetails loadByUserId(Long id) {
        User u = users.findById(id).orElseThrow(() -> new ApiException(401, "user not found"));
        return CustomUserDetails.from(u);
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        var u = users.findByUsername(username).orElseThrow(() -> new ApiException(401, "user not found"));
        return CustomUserDetails.from(u);
    }
}