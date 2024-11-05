package housit.housit_backend.security;

//import jwt.jwtspringsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CorsConfig corsConfig;
//    private final JwtUtil jwtUtil;
//    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 사용자 인증을 수행하는 빈
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // CSRF 보안 설정 비활성화
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음
                .addFilter(corsConfig.corsFilter()) // @CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O) --> 모든 요청 허용.
                .formLogin(AbstractHttpConfigurer::disable) // Form 로그인 사용하지 않음, Spring Security 에서 기본 제공하는 /login 페이지가 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // Basic 인증 사용하지 않음, 대신 Bearer 토큰 사용 (JWT)
//                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtUtil))
//                .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository, jwtUtil))
                //.addFilterBefore(jwtRequestFilter, BasicAuthenticationFilter.class) // JWT 필터 추가
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/api/v1/user/**").authenticated() // /user라는 url로 들어오면 인증이 필요하다.
//                        .requestMatchers("/api/v1/manager/**").hasAnyRole("MANAGER", "ADMIN") // manager으로 들어오는 MANAGER 인증 또는 ADMIN인증이 필요하다는 뜻이다.
//                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN") // //admin으로 들어오면 ADMIN권한이 있는 사람만 들어올 수 있음
                        .anyRequest().permitAll() // 그리고 나머지 url은 전부 권한을 허용해준다.
                );
        return http.build();
    }

}
