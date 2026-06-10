package com.example.stock;

import com.example.stock.config.StockProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StockProperties.class)
public class StockManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockManagementSystemApplication.class, args);
    }
}
