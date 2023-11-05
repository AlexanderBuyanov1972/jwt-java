package com.example.demo.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
public class CookieService {
    @Value("#{T(Integer).parseInt('${age.max.cookie}')}")
    private int ageMaxCookie;

    public Cookie addCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(ageMaxCookie);
        return cookie;
    }

    public Cookie getCookie(String name, HttpServletRequest request) {
        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cook : cookies) {
                if (name.equals(cook.getName())) {
                    cookie = cook;
                    break;
                }
            }
        }
        assert cookie != null;
        return cookie;
    }
}
