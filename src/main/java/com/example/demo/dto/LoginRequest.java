package com.example.demo.dto;

import lombok.*;

@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}

