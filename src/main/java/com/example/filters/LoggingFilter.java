package com.example.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        LocalDateTime time = LocalDateTime.now();

        log.info("Incoming request: method={}, uri={}, ip={}, time={}", method, uri, ip, time);

        filterChain.doFilter(request, response);

        int status = response.getStatus();
        log.info("Outgoing response: status={}, uri={}, ip={}, time={}", status, uri, ip, LocalDateTime.now());
    }
}