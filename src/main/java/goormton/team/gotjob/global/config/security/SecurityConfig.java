package goormton.team.gotjob.global.config.security;

import goormton.team.gotjob.global.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)

public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final RestAuthenticationEntryPoint entryPoint;


    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**",
            "/api-docs",
            "/swagger-ui-custom.html",
            "/v3/api-docs/**",
            "/api-docs/**",
            "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
//                cors 설정
                .cors(Customizer.withDefaults())
//                crsf disable
                .csrf(AbstractHttpConfigurer::disable)
//                Form 로그인 방식 disable
                .formLogin(AbstractHttpConfigurer::disable)
//                Http Basic 인증 방식 disable
                .httpBasic(AbstractHttpConfigurer::disable)
//                세션 설정: STATELESS
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 401 응답(JSON)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint))
//                경로별 인가 작업
                .authorizeHttpRequests((auth) -> auth
//                              미리 정의된 whitelist 경로는 모두 허용
                                .requestMatchers(AUTH_WHITELIST).permitAll()
//                              모든 get method는 허용
                                .requestMatchers(HttpMethod.GET, "/**").permitAll()
//                              유저 인증용 post method는 인증 없이 허용
                                .requestMatchers(HttpMethod.POST, "/api/auth/**", "/oauth2/**").permitAll()
//                              그 외의 모든 post method는 인증 필요
                                .requestMatchers(HttpMethod.POST, "/**").permitAll()
//                                혹시 모를 다른 모든 요청 역시 인증 필요
                                .anyRequest().authenticated()
                )
        ;
        // JWT 필터 추가
        http.addFilterBefore(
                new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService),
                UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
