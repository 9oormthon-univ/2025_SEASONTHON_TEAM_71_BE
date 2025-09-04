package goormton.team.gotjob.domain.consultant.service;
import goormton.team.gotjob.domain.consultant.dto.*;
import goormton.team.gotjob.domain.consultant.domain.ConsultantMatch;
import goormton.team.gotjob.domain.consultant.repository.ConsultantMatchRepository;
import goormton.team.gotjob.domain.user.domain.User;
import goormton.team.gotjob.domain.user.repository.UserRepository;
import goormton.team.gotjob.domain.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;



@Service
@RequiredArgsConstructor
public class ConsultantMatchService {
    private final ConsultantMatchRepository matches; private final UserRepository users;

    @Transactional
    public ConsultantMatchResponse create(ConsultantMatchCreateRequest req){
        User c = users.findById(req.consultantId()).orElseThrow(()->new ApiException(404,"consultant not found"));
        User u = users.findById(req.userId()).orElseThrow(()->new ApiException(404,"user not found"));
        matches.findByConsultantAndUser(c,u).ifPresent(m->{ throw new ApiException(409,"already matched"); });
        var m = matches.save(ConsultantMatch.builder().consultant(c).user(u).status("ACTIVE").note(req.note()).build());
        return toDto(m);
    }

    @Transactional(readOnly = true)
    public List<ConsultantMatchResponse> list(Long consultantId, Long userId){
        if(consultantId!=null) return matches.findByConsultantId(consultantId).stream().map(this::toDto).toList();
        if(userId!=null) return matches.findByUserId(userId).stream().map(this::toDto).toList();
        throw new ApiException(400,"consultantId or userId required");
    }

    @Transactional
    public ConsultantMatchResponse update(Long id, ConsultantMatchUpdateRequest req){
        var m = matches.findById(id).orElseThrow(()->new ApiException(404,"match not found"));
        m.setStatus(req.status());
        return toDto(m);
    }

    private ConsultantMatchResponse toDto(ConsultantMatch m){
        var ts = m.getCreatedAt()!=null? m.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME):null;
        return new ConsultantMatchResponse(m.getId(), m.getConsultant().getId(), m.getUser().getId(), m.getStatus(), m.getNote(), ts);
    }
}