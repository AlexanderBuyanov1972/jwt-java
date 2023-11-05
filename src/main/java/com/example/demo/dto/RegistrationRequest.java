package com.example.demo.dto;

import lombok.*;

import java.util.List;

@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegistrationRequest {
    private String username;
    private String email;
    private String password;
    private String role;
}

