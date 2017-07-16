package com.wit.fgj.runtime.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 在slf4j {@link MDC}中设置{@code username}和{@code times}变量。
 *
 * @author yangwu
 *
 */
public class MdcFilter implements Filter {

    private static Long count = 0L;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            MDC.put("username", authentication == null ? "" : authentication.getName());

            Long cnt;
            synchronized (count) {
                cnt = ++count;
            }
            MDC.put("times", cnt.toString());

            filterChain.doFilter(servletRequest, servletResponse);
        }
        finally {
            MDC.remove("times");
            MDC.remove("username");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
