package housit.housit_backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final CorsFilter corsFilter;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // CSRF 보안 설정 비활성화
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음
                .formLogin(AbstractHttpConfigurer::disable) // Form 로그인 사용하지 않음
                .httpBasic(AbstractHttpConfigurer::disable) // Basic 인증 사용하지 않음, 대신 Bearer 토큰 사용 (JWT)
                .addFilter(corsFilter)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/room", "/room/{roomId}", "/room/{roomId}/member",
                                "/room/{roomId}/member/{memberId}",
                                "/room/{roomId}/member/{memberId}/join").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, BasicAuthenticationFilter.class); // JWT 필터 추가

        return http.build();
    }

}
