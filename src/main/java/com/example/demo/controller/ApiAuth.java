package com.example.demo.controller;

public interface ApiAuth {
    String URL_SERVER = "http://localhost:8080";
    String ALLOWED_SERVER = "*";
    String ALLOWED_HEADERS = "*";
    String LINK = "/{link}";
    String EMAIL = "/{email}";
    String REGISTRATION = "/registration";
    String LOGIN = "/login";
    String LOGOUT = "/exit";
    String ACTIVATE = "/activate";
    String REFRESH = "/refresh";
    String CHECK = "/check";
}