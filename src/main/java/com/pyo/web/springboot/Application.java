package com.pyo.web.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // jpa auditing 활성화 어노테이션
@SpringBootApplication
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class , args);
    }

}
