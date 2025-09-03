package goormton.team.gotjob.domain.job.dto;

import java.util.List;

public record JobSearchParams(String q, String location, List<String> skills,
                              Integer page, Integer size) {}