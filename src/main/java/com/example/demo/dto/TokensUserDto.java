package com.example.demo.dto;

import lombok.*;

import java.util.Date;

@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokensUserDto {
    private String accessToken;
    private Date accessTokenIssuedAt;
    private Date accessTokenExpiredAt;
    private String refreshToken;
    private Date refreshTokenIssuedAt;
    private Date refreshTokenExpiredAt;
    private UserDto userDto;
}

