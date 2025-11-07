package com.ateam.calmate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@MapperScan("com.ateam.calmate.community.query.mapper")
public class MountainspiritApplication {

    public static void main(String[] args) {
        SpringApplication.run(MountainspiritApplication.class, args);
    }

}
