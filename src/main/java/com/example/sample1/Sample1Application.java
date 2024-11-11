package com.example.sample1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan(basePackages = "com.example.sample1.entity")  // 엔티티 클래스의 패키지 경로 지정
public class Sample1Application {

    public static void main(String[] args) {
        SpringApplication.run(Sample1Application.class, args);
    }

}
