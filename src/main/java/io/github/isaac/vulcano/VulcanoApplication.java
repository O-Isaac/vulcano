package io.github.isaac.vulcano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VulcanoApplication {

    static void main(String[] args) {
        SpringApplication.run(VulcanoApplication.class, args);
    }

}
