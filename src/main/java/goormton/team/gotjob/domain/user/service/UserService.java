package goormton.team.gotjob.domain.user.service;

import goormton.team.gotjob.domain.user.dto.*;
import goormton.team.gotjob.domain.user.domain.UserProfile;
import goormton.team.gotjob.domain.user.repository.*;
import goormton.team.gotjob.domain.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class UserService {
    private final UserRepository users;
    private final UserProfileRepository profiles;

    @Transactional(readOnly = true)
    public MeResponse me(Long userId){
        var u = users.findById(userId).orElseThrow(()->new ApiException(404,"user not found"));
        var p = u.getProfile();
        return new MeResponse(u.getId(),u.getUsername(),u.getEmail(),u.getRole().name(),
                p!=null?p.getBio():null, p!=null?p.getYearsExperience():null, p!=null?p.getLocation():null,
                p!=null?p.getResumeUrl():null, p!=null?p.getPortfolioUrl():null);
    }

    @Transactional
    public MeResponse update(Long userId, UpdateMeRequest req){
        var u = users.findById(userId).orElseThrow(()->new ApiException(404,"user not found"));
        var p = u.getProfile();
        if(p==null){ p = new UserProfile(); p.setUser(u); u.setProfile(p); }
        p.setBio(req.bio()); p.setYearsExperience(req.yearsExperience());
        p.setLocation(req.location()); p.setResumeUrl(req.resumeUrl()); p.setPortfolioUrl(req.portfolioUrl());
        profiles.save(p);
        return me(userId);
    }
}