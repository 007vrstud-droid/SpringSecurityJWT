package com.example.controllers;

import com.example.config.JWTUtils;
import com.example.dto.AuthRequest;
import com.example.services.LoginService;
import com.example.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userDetailsService;
    private final JWTUtils jwtUtils;
    private final LoginService loginAttemptService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.username(),
                            authRequest.password()
                    )
            );

            loginAttemptService.loginSucceeded(authRequest.username());

            UserDetails user = userDetailsService.loadUserByUsername(authRequest.username());
            String token = jwtUtils.generateToken(user);

            return ResponseEntity.ok(token);

        } catch (LockedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body("Аккаунт заблокирован");

        } catch (BadCredentialsException e) {
            loginAttemptService.loginFailed(authRequest.username());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
        }
    }

}