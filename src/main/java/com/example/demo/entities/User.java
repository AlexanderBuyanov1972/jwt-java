package com.example.demo.entities;

import com.example.demo.enums.TypoOfRoles;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypoOfRoles role;
    @Column(name = "activationLink", nullable = false)
    private String activationLink;
    @Column(name = "is_activity", nullable = false)
    private boolean isActivated;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}

