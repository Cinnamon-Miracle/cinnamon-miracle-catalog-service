package com.cinnamonmiracle.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.cinnamonmiracle")
@EnableJpaAuditing
public class CatalogServiceApplication {
    public static void main(String[] args) {
        System.setProperty("net.bytebuddy.experimental", "true");
        SpringApplication.run(CatalogServiceApplication.class, args);
    }
}
