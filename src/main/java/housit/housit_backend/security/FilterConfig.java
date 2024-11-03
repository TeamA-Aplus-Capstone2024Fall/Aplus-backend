package housit.housit_backend.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<MyFilter> filter() {
        FilterRegistrationBean<MyFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.addUrlPatterns("/*");
        filterBean.setOrder(0);
        return filterBean;
    }
}
