package com.example.demo.controller;

import com.example.demo.services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin(maxAge = 3600, origins = "*")
@RestController
@RequestMapping()
public class AuthController {

    @Autowired
    private AuthService appService;

    @CrossOrigin(origins = ApiAuth.ALLOWED_SERVER, allowedHeaders = ApiAuth.ALLOWED_HEADERS)
    @PostMapping(value = ApiAuth.REGISTRATION)
    @ResponseBody
    public Object registration(HttpServletRequest request) {
        return appService.registration(request);
    }

    @CrossOrigin(origins = ApiAuth.ALLOWED_SERVER, allowedHeaders = ApiAuth.ALLOWED_HEADERS)
    @PostMapping(value = ApiAuth.LOGIN)
    @ResponseBody
    public Object login(HttpServletRequest request) {
        return appService.login(request);
    }

    @CrossOrigin(origins = ApiAuth.ALLOWED_SERVER, allowedHeaders = ApiAuth.ALLOWED_HEADERS)
    @GetMapping(value = ApiAuth.LOGOUT)
    public Object logout(HttpServletRequest request, HttpServletResponse response) {
        return appService.logout(request, response);
    }

    @CrossOrigin(origins = ApiAuth.ALLOWED_SERVER, allowedHeaders = ApiAuth.ALLOWED_HEADERS)
    @GetMapping(value = ApiAuth.ACTIVATE + ApiAuth.LINK)
    public Object activate(HttpServletRequest request) {
        return appService.activate(request);
    }

    @CrossOrigin(origins = ApiAuth.ALLOWED_SERVER, allowedHeaders = ApiAuth.ALLOWED_HEADERS)
    @GetMapping(value = ApiAuth.REFRESH)
    @ResponseBody
    public Object refresh(HttpServletRequest request) {
        return appService.refresh(request);
    }

    @CrossOrigin(origins = ApiAuth.ALLOWED_SERVER, allowedHeaders = ApiAuth.ALLOWED_HEADERS)
    @GetMapping(value = ApiAuth.CHECK + ApiAuth.EMAIL)
    @ResponseBody
    public Object check(HttpServletRequest request) {
        return appService.check(request);
    }
}


