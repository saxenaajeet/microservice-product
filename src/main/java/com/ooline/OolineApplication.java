package com.ooline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class OolineApplication {
    public static void main(String[] args) {
        SpringApplication.run(OolineApplication.class, args);
    }
}
