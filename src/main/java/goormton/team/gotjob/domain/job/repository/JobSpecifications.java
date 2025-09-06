package goormton.team.gotjob.domain.job.repository;


import goormton.team.gotjob.domain.job.domain.Job;
import goormton.team.gotjob.domain.job.dto.JobSearchParams;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class JobSpecifications {

    private JobSpecifications() {}

    public static Specification<Job> search(JobSearchParams p) {
        if (p == null) return alwaysTrue();

        List<Specification<Job>> specs = new ArrayList<>();
        var s1 = qLike(p.q());                 if (s1 != null) specs.add(s1);
        var s2 = locationEq(p.location());     if (s2 != null) specs.add(s2);
        var s3 = employmentTypeEq(p.employmentType()); if (s3 != null) specs.add(s3);
        var s4 = statusEq(p.status());         if (s4 != null) specs.add(s4);

        if (specs.isEmpty()) {
            return alwaysTrue(); // 아무 필터도 없으면 전체 검색
        }
        // Iterable 오버로드 사용 (빈 리스트 아님)
        return Specification.allOf(specs);
    }

    private static Specification<Job> alwaysTrue() {
        return (root, cq, cb) -> cb.conjunction();
    }

    private static Specification<Job> qLike(String q) {
        if (q == null || q.isBlank()) return null;
        String qq = "%" + q.toLowerCase() + "%";
        return (root, cq, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), qq),
                cb.like(cb.lower(root.get("description")), qq),
                cb.like(cb.lower(root.get("requirements")), qq)
        );
    }

    private static Specification<Job> locationEq(String loc) {
        if (loc == null || loc.isBlank()) return null;
        return (root, cq, cb) -> cb.equal(root.get("location"), loc);
    }

    private static Specification<Job> employmentTypeEq(String type) {
        if (type == null || type.isBlank()) return null;
        return (root, cq, cb) -> cb.equal(root.get("employmentType"), type);
    }

    private static Specification<Job> statusEq(String status) {
        if (status == null || status.isBlank()) return null;
        return (root, cq, cb) -> cb.equal(root.get("status"), status);
    }
}
