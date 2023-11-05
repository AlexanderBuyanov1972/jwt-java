package com.example.demo.dto;


import com.example.demo.entities.User;
import lombok.*;

@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private String username;
    private String email;
    private String role;
    private boolean isActivated;
    private String activationLink;

    public UserDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole().toString();
        this.isActivated = user.isActivated();
        this.activationLink = user.getActivationLink();
    }


}
