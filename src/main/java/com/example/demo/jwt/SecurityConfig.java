package com.example.demo.jwt;

import com.example.demo.controller.ApiAuth;


import com.example.demo.jwt.filters.AuthenticationFilter;
import com.example.demo.jwt.filters.AuthorizationFilter;
import com.example.demo.jwt.filters.RefreshFilter;
import com.example.demo.jwt.user_details.CustomUserDetailsService;
import com.example.demo.services.CookieService;
import com.example.demo.services.TokenService;
import com.example.demo.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenService tokenService;
    private final CookieService cookieService;
    private final UserService userService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          TokenService tokenService,
                          CookieService cookieService,
                          UserService userService) {
        this.userService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.customUserDetailsService = customUserDetailsService;
        this.tokenService = tokenService;
        this.cookieService = cookieService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        http.cors();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling().authenticationEntryPoint(unauthorizedResponse());
        // filters
        http.addFilter(new AuthenticationFilter(authenticationManagerBean(), userService, tokenService, cookieService));
        http.addFilter(new RefreshFilter(authenticationManagerBean(), tokenService, cookieService, userService));
        http.addFilterAfter(new AuthorizationFilter(tokenService, userService), UsernamePasswordAuthenticationFilter.class);
        // -------------------- permitAll() ----------------------------------------------------
        http.authorizeRequests().antMatchers(HttpMethod.POST, ApiAuth.REGISTRATION).permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, ApiAuth.LOGIN).permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, ApiAuth.ACTIVATE + "/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, ApiAuth.CHECK + "/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, ApiAuth.REFRESH).permitAll();
        // ------------------- hasAnyRole("ADMIN", "USER") -------------------------------------
        http.authorizeRequests().antMatchers(HttpMethod.GET, ApiAuth.LOGOUT).hasAnyRole("ADMIN", "USER");
    }

    private AuthenticationEntryPoint unauthorizedResponse() {
        return (req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }

}
