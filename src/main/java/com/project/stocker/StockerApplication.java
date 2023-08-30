package com.project.stocker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class StockerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockerApplication.class, args);
    }

}
