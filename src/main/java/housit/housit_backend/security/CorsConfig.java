package housit.housit_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() { // CorsFilter 는 apache.catalina 가 아닌 springframework.web.filter 패키지에 있는 클래스
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 자격 증명 허용 (쿠키, Authorization 헤더 등)
        config.addAllowedOriginPattern("*"); // 모든 도메인 허용
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
        config.addAllowedOrigin("*");
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 위 설정 적용
        return new CorsFilter(source); // source 전달하여 CorsFilter 생성
    }
}
