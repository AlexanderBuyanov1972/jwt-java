package com.example.demo.entities;

import lombok.*;

import javax.persistence.*;

@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "token", nullable = false)
    private String token;

}