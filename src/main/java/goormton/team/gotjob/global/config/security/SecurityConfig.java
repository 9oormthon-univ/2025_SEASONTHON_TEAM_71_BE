package goormton.team.gotjob.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**",
            "/api-docs",
            "/swagger-ui-custom.html",
            "/v3/api-docs/**",
            "/api-docs/**",
            "/swagger-ui.html"
    };

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        // application.yml 값 반영이 어렵다면, 바로 하드코딩도 가능:
        cfg.setAllowedOrigins(java.util.List.of(
                "http://localhost:3000",
                "http://localhost:8080",
                "http://13.125.205.40:8080",
                "http://localhost:5173"
        ));
        cfg.setAllowedMethods(java.util.List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(java.util.List.of("*"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
//                cors 설정
                .cors(c -> c.configurationSource(corsConfigurationSource()))
//                crsf disable
                .csrf(AbstractHttpConfigurer::disable)
//                Form 로그인 방식 disable
                .formLogin(AbstractHttpConfigurer::disable)
//                Http Basic 인증 방식 disable
                .httpBasic(AbstractHttpConfigurer::disable)
//                세션 설정: STATELESS
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                경로별 인가 작업
                .authorizeHttpRequests((auth) -> auth
//                              미리 정의된 whitelist 경로는 모두 허용
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
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
