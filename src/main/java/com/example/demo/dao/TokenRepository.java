package com.example.demo.dao;


import com.example.demo.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface TokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findTokenByEmail(String email);
    void removeByEmail(String email);
}
