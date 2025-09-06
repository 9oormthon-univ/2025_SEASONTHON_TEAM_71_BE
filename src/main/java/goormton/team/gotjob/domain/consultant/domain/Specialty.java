package goormton.team.gotjob.domain.consultant.domain;

import goormton.team.gotjob.domain.common.BaseEntity;
import goormton.team.gotjob.domain.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Specialty extends BaseEntity {

    @ManyToOne @JoinColumn(name = "user_id")
    User user;

    private String keyword;
}
