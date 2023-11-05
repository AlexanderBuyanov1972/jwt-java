package com.example.demo.jwt.filters;

import com.example.demo.controller.ApiAuth;
import com.example.demo.dto.LoginRequest;
import com.example.demo.entities.User;

import com.example.demo.services.CookieService;
import com.example.demo.services.TokenService;
import com.example.demo.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger log = LogManager.getLogger("Class - AuthenticationFilter");
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;
    private final CookieService cookieService;


    public AuthenticationFilter(
            AuthenticationManager authenticationManager,
            UserService userService,
            TokenService tokenService,
            CookieService cookieService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
        this.cookieService = cookieService;
        this.objectMapper = new ObjectMapper();
        setLoginPath(ApiAuth.LOGIN);
    }

    private void setLoginPath(String path) {
        setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher(path, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequest credentials = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            log.info("credentials : " + credentials);
            request.setAttribute("password", credentials.getPassword());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword(),
                            Collections.emptyList()
                    ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        log.info("auth : " + auth);
        String email = auth.getName();
        log.info("auth.getCredentials() : " + auth.getCredentials());
        String password = request.getAttribute("password").toString();
        User user = userService.getUserByEmail(email);
        if (user != null) {
            String refreshToken = "Bearer " + tokenService.createTokenRefresh(user, password);
            tokenService.removeTokenByEmail(user.getEmail());
            tokenService.saveRefreshToken(user.getEmail(), refreshToken);
            Cookie cookieRefresh = cookieService.addCookie("refreshToken", refreshToken.substring(7));
            response.addCookie(cookieRefresh);
        }
        request.setAttribute("email", email);
        request.setAttribute("password","*****");
        chain.doFilter(request, response);
    }

}
