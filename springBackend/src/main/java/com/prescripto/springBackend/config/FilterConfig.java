package com.prescripto.springBackend.config;

import com.prescripto.springBackend.filter.AuthUserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthUserFilter> authUserFilter() {
        FilterRegistrationBean<AuthUserFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthUserFilter());
        registrationBean.addUrlPatterns("/api/user/get-profile"); // apply filter to these routes
        return registrationBean;
    }
}

