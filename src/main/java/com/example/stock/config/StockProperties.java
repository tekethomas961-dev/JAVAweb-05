package com.example.stock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stock")
public class StockProperties {

    private int warnThreshold = 10;

    public int getWarnThreshold() {
        return warnThreshold;
    }

    public void setWarnThreshold(int warnThreshold) {
        this.warnThreshold = warnThreshold;
    }
}
