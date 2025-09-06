package goormton.team.gotjob.domain.user.dto;

public record SignupRequest(String username, String password, String email,
                            String realName, String phone, String role) {}
