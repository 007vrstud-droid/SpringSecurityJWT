package com.example.filters;


import com.example.config.JWTUtils;
import com.example.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Извлечение заголовка авторизации из запроса
        final String authHeader = request.getHeader("Authorization");

        // Проверка наличия заголовка авторизации
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Извлечение токена из заголовка
        final String jwtToken = authHeader.substring("Bearer ".length());

        // Извлечение имени пользователя из JWT токена
        final String username = jwtUtils.extractUsername(jwtToken);

        // Если имя пользователя не пустое и токен валиден
        if (username != null && jwtUtils.isTokenValid(jwtToken, userService.loadUserByUsername(username))) {
            UserDetails userDetails = userService.loadUserByUsername(username);

            // Создание аутентификации и установка в контекст
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            // Токен невалиден — передаем запрос дальше
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Продолжение цепочки фильтров
        filterChain.doFilter(request, response);
    }
}
