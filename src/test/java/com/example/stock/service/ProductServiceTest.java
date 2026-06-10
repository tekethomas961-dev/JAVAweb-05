package com.example.stock.service;

import com.example.stock.domain.Product;
import com.example.stock.exception.InsufficientStockException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Rollback
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void addProductReturnsProductWithGeneratedId() {
        Product product = productService.addProduct(new Product(null, "矿泉水", 60));

        assertThat(product.getId()).isNotNull();
        assertThat(productService.getProduct(product.getId()).getName()).isEqualTo("矿泉水");
    }

    @Test
    void outboundProductUpdatesStockWhenStockIsEnough() {
        Product product = productService.outboundProduct(1, 15);

        assertThat(product.getStock()).isEqualTo(85);
        assertThat(productService.getProduct(1).getStock()).isEqualTo(85);
    }

    @Test
    void outboundProductThrowsWhenStockIsInsufficient() {
        assertThatThrownBy(() -> productService.outboundProduct(3, 99))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("库存不足");
    }

    @Test
    void searchProductsUsesDynamicSqlConditions() {
        List<Product> products = productService.searchProducts("奶", 30);

        assertThat(products)
                .extracting(Product::getName)
                .containsExactly("牛奶");
    }
}
