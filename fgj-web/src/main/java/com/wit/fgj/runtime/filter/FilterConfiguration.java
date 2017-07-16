package com.wit.fgj.runtime.filter;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置Servlet Filter，其执行顺序为：
 *
 * <ul>
 * <li>wit requestContextFilter</li>
 * <li>wit logFilter</li>
 * <li>spring security</li>
 * </ul>
 *
 * @author yw
 *
 */
@Configuration
public class FilterConfiguration {

    private static final int MDC_FILTER_ORDER = SecurityProperties.DEFAULT_FILTER_ORDER + 100;
    private static final int LOG_FILTER_ORDER = MDC_FILTER_ORDER + 10;

    // mdcFilter

    @Bean
    public MdcFilter fxpMdcFilter() {
        return new MdcFilter();
    }

    @Bean
    public FilterRegistrationBean fxpRequestContextFilterRegistration(MdcFilter filter) {
        FilterRegistrationBean reg = new FilterRegistrationBean(filter);
        reg.addUrlPatterns("/*");
        reg.setOrder(MDC_FILTER_ORDER);
        return reg;
    }

    // logFilter

    @Bean
    public LogFilter logFilter() {
        return new LogFilter();
    }

    @Bean
    public FilterRegistrationBean logFilterRegistration(LogFilter filter) {
        FilterRegistrationBean reg = new FilterRegistrationBean(filter);
        reg.addUrlPatterns("/*");
        reg.setOrder(LOG_FILTER_ORDER);
        return reg;
    }

}
