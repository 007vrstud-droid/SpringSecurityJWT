package com.example.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("Incoming request: method={}, uri={}, ip={}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        filterChain.doFilter(request, response);
        log.info("Outgoing response: status={}, uri={}, ip={}", response.getStatus(), request.getRequestURI(), request.getRemoteAddr());
    }
}
