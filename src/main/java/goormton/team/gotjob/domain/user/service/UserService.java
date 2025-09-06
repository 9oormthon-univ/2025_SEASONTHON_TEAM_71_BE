package goormton.team.gotjob.domain.user.service;

import goormton.team.gotjob.domain.user.dto.*;
import goormton.team.gotjob.domain.user.repository.*;
import goormton.team.gotjob.domain.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository users;

    @Transactional(readOnly = true)
    public MeResponse me(Long userId){
        var u = users.findById(userId).orElseThrow(() -> new ApiException(404,"user not found"));
        return new MeResponse(
                u.getId(), u.getUsername(), u.getEmail(), u.getRole().name(),
                u.getRealName(), u.getPhone(),
                u.getInterestJob(), u.getSkills(),
                u.getBio(), u.getResumeUrl()
        );
    }

    @Transactional
    public MeResponse update(Long userId, UpdateMeRequest req){
        var u = users.findById(userId).orElseThrow(() -> new ApiException(404,"user not found"));

        u.setInterestJob(req.interestJob());
        u.setSkills(req.skills());
        u.setBio(req.bio());
        u.setResumeUrl(req.resumeUrl());

        users.save(u);
        return me(userId);
    }
}
