package com.example.stock.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockLog {

    private Long logId;
    private Integer productId;
    private Integer outQuantity;
    private LocalDateTime operateTime;
    private String result;
}
