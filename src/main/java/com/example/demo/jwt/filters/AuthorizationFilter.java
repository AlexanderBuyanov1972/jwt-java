package com.example.demo.jwt.filters;

import com.example.demo.entities.User;
import com.example.demo.jwt.user_details.CustomUserDetails;
import com.example.demo.services.TokenService;
import com.example.demo.services.UserService;
import io.jsonwebtoken.Claims;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends OncePerRequestFilter {
    private static final Logger log = LogManager.getLogger("Class - AuthorizationFilter");
    private final TokenService tokenService;
    private final UserService userService;

    public AuthorizationFilter(
            TokenService tokenService,
            UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        String valueHeader = httpServletRequest.getHeader("Authorization");
        if (tokenService.validateToken(valueHeader)) {
            try {
                Claims claims = tokenService.getClaimsAccess(valueHeader.substring(7));
                log.info(claims);
                String email = claims.getSubject();
                log.info(email);
                User user = userService.getUserByEmail(email);
                log.info(user);
                CustomUserDetails customUserDetails = CustomUserDetails.fromUserEntityToCustomUserDetails(user);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                customUserDetails.getAuthorities()
                        ));
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
