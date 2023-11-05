package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication(exclude = {SolrAutoConfiguration.class})
public class JwtJavaApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwtJavaApplication.class, args);
    }
}
