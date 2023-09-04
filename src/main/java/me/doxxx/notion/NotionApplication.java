package me.doxxx.notion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NotionApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotionApplication.class, args);
    }

}
