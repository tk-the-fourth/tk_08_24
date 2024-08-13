package com.pointOfSale.src;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaRepositories
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.pointOfSale.src"})
@EnableAutoConfiguration
@EnableAsync
public class PointOfSaleApplication {
   public static void main(String[] args) {
      SpringApplication.run(PointOfSaleApplication.class, args);
   }
}

