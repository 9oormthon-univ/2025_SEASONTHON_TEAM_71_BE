package goormton.team.gotjob.domain.bookmark.domain;
import goormton.team.gotjob.domain.user.domain.User;
import goormton.team.gotjob.domain.job.domain.Job;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="bookmarks",uniqueConstraints=@UniqueConstraint(columnNames={"user_id","job_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bookmark {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="job_id")
    private Job job;
}