package com.example.stock.service;

import com.example.stock.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class ProductTransactionRollbackTest {

    @Autowired
    private ProductService productService;

    @Test
    void logFailureRollsBackStockUpdate() {
        Product before = productService.getProduct(2);

        assertThatThrownBy(() -> productService.outboundProduct(2, 5, true))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("模拟日志写入后发生故障");

        Product after = productService.getProduct(2);
        assertThat(after.getStock()).isEqualTo(before.getStock());
    }
}
