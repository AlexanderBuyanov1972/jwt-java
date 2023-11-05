package com.example.demo.services;

import com.example.demo.controller.ApiAuth;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.TokensUserDto;
import com.example.demo.dto.RegistrationRequest;
import com.example.demo.entities.RefreshToken;
import com.example.demo.entities.User;
import com.example.demo.enums.TypoOfRoles;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.*;

@Service
public class AuthService {
    private static final Logger log = LogManager.getLogger("AppService");
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final MailService mailService;
    private final CookieService cookieService;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;

    public AuthService(BCryptPasswordEncoder passwordEncoder,
                       UserService userService,
                       MailService mailService,
                       CookieService cookieService,
                       TokenService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.mailService = mailService;
        this.cookieService = cookieService;
        this.tokenService = tokenService;
        this.objectMapper = new ObjectMapper();
    }


    public Object registration(HttpServletRequest request) {
        log.info("Entered the registration method.");
        User userDB = null;
        try {
            RegistrationRequest body = objectMapper.readValue(request.getInputStream(), RegistrationRequest.class);
            if (body != null) {
                userDB = userService.getUserByEmail(body.getEmail());
                if (userDB == null) {
                    String hashPassword = passwordEncoder.encode(body.getPassword());
                    String activationLink = UUID.randomUUID().toString().replace("-", "");
                    String path = ApiAuth.URL_SERVER + ApiAuth.ACTIVATE + "/" + activationLink;
                    User user = User
                            .builder()
                            .username(body.getUsername())
                            .email(body.getEmail())
                            .role(TypoOfRoles.valueOf(body.getRole()))
                            .activationLink(activationLink)
                            .password(hashPassword)
                            .isActivated(false)
                            .build();
                    userService.createUser(user);
                    mailService.sendActivationMail(path);
                    log.info("Registration completed successfully.");
                    return new UserDto(user);
                }
            } else {
                log.info("Request is not contains body.");
                return List.of("Request is not contains body.");
            }

        } catch (Exception error) {
            log.error("Unexpected server error.");
            return List.of("Unexpected server error.", error.getMessage());
        }
        log.info("You have already been registered.");
        return List.of("You have already been registered.");
    }

    public Object login(HttpServletRequest request) {
        log.info("Entered the login method.");
        try {
            String email = request.getAttribute("email").toString();
            log.info("email : " + email);
            return doMain(email);
        } catch (Exception error) {
            log.error("Unexpected server error.");
            log.error(error.getMessage());
            return List.of("Unexpected server error.", error.getMessage());
        }

    }

    public Object logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Entered the logout method.");
        try {
            String accessToken = request.getHeader("Authorization");
            log.info("accessToken : " + accessToken);
            if (tokenService.validateToken(accessToken)) {
                Claims claims = tokenService.getClaimsAccess(accessToken.substring(7));
                String email = claims.getSubject();
                if (email == null) {
                    log.error("Email invalid or is not exists.");
                    return List.of("Email invalid or is not exists.");
                }
                tokenService.removeTokenByEmail(email);
                Cookie cookie = cookieService.getCookie("refreshToken", request);
                if (cookie == null) {
                    log.info("Cookie file is missing or damaged.");
                    return List.of("Cookie file is missing or damaged.");
                }
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                log.error("Logout successfully");
                return List.of("Logout successfully");
            }
            return List.of("Access token invalid or is not exists");
        } catch (Exception error) {
            log.error("Unexpected server error.");
            return List.of("Unexpected server error.", error.getMessage());
        }
    }

    public Object activate(HttpServletRequest request) {
        log.info("Entered the mail activation method.");
        try {
            String url = new URL(request.getRequestURL().toString()).toString();
            String link = url.substring(url.lastIndexOf("/") + 1).trim();
            User user = userService.getUserByActivationLink(link);
            if (user != null) {
                user.setActivated(true);
                userService.createUser(user);
                log.error("Activation was successful.");
                return new UserDto(user);
            }
        } catch (Exception error) {
            log.error("Unexpected server error.");
            return List.of("Unexpected server error.", error.getMessage());
        }
        return List.of("The user for this activation link does not exist");
    }

    public Object refresh(HttpServletRequest request) {
        log.info("Entered the refresh method.");
        try {
            String email = request.getAttribute("email").toString();
            return doMain(email);
        } catch (Exception error) {
            log.error("Unexpected server error.");
            return List.of("Unexpected server error.", error.getMessage());
        }
    }

    public Object check(HttpServletRequest request) {
        log.info("Entered user verification method.");
        try {
            String url = new URL(request.getRequestURL().toString()).toString();
            String email = url.substring(url.lastIndexOf("/") + 1).trim();
            return doMain(email);
        } catch (Exception error) {
            log.error("Unexpected server error.");
            return List.of("Unexpected server error.", error.getMessage());
        }
    }

    private Object doMain(String email) {
        log.error("Entered the doMain method.");
        // --------------------------------------------------------
        User user = userService.getUserByEmail(email);
        if (user != null) {
            String refreshToken = tokenService.getTokenByEmail(email).getToken();
            if (refreshToken != null) {
                String accessToken = "Bearer " + tokenService.createTokenAccess(
                        user);
                Claims claimsAccessToken = tokenService.getClaimsAccess(accessToken.substring(7));
                log.info("claimsAccessToken : " + claimsAccessToken);
                Claims claimsRefreshToken = tokenService.getClaimsRefresh(refreshToken.substring(7));
                log.info("claimsRefreshToken : " + claimsRefreshToken);
                log.info("Authorization completed successfully.");
                return TokensUserDto
                        .builder()
                        .accessToken(accessToken)
                        .accessTokenIssuedAt(claimsAccessToken.getIssuedAt())
                        .accessTokenExpiredAt(claimsAccessToken.getExpiration())
                        .refreshToken(refreshToken)
                        .refreshTokenIssuedAt(claimsRefreshToken.getIssuedAt())
                        .refreshTokenExpiredAt(claimsRefreshToken.getExpiration())
                        .userDto(new UserDto(user))
                        .build();
            }
            log.error("The user is not authorized.");
            return List.of("The user is not authorized.");
        }
        log.info("User with that email is not exists.");
        return List.of("User with that email is not exists.");
    }
}