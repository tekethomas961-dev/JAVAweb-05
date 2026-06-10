package com.example.stock.service;

import com.example.stock.config.StockProperties;
import com.example.stock.domain.Product;
import com.example.stock.domain.StockLog;
import com.example.stock.exception.InsufficientStockException;
import com.example.stock.mapper.ProductMapper;
import com.example.stock.mapper.StockLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductMapper productMapper;
    private final StockLogMapper stockLogMapper;
    private final StockProperties stockProperties;

    public ProductService(ProductMapper productMapper, StockLogMapper stockLogMapper, StockProperties stockProperties) {
        this.productMapper = productMapper;
        this.stockLogMapper = stockLogMapper;
        this.stockProperties = stockProperties;
    }

    public Product addProduct(Product product) {
        productMapper.insert(product);
        return product;
    }

    @Transactional(rollbackFor = Exception.class)
    public Product outboundProduct(Integer id, Integer quantity) {
        return outboundProduct(id, quantity, false);
    }

    @Transactional(rollbackFor = Exception.class)
    public Product outboundProduct(Integer id, Integer quantity, boolean simulateLogFailure) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("出库数量必须大于 0");
        }

        Product product = productMapper.findById(id);
        if (product == null) {
            throw new IllegalArgumentException("商品不存在，id=" + id);
        }
        if (product.getStock() < quantity) {
            throw new InsufficientStockException("库存不足，当前库存：" + product.getStock() + "，出库数量：" + quantity);
        }

        int newStock = product.getStock() - quantity;
        productMapper.updateStock(id, newStock);

        StockLog stockLog = new StockLog(null, id, quantity, LocalDateTime.now(), "成功");
        stockLogMapper.insert(stockLog);
        if (simulateLogFailure) {
            throw new IllegalStateException("模拟日志写入后发生故障，验证事务回滚");
        }

        if (newStock < stockProperties.getWarnThreshold()) {
            log.warn("库存预警：商品 [{}] 当前库存 {} 低于阈值 {}", product.getName(), newStock, stockProperties.getWarnThreshold());
        }

        product.setStock(newStock);
        return product;
    }

    public List<Product> getAllProducts() {
        return productMapper.findAll();
    }

    public Product getProduct(Integer id) {
        return productMapper.findById(id);
    }

    public List<Product> searchProducts(String name, Integer minStock) {
        return productMapper.searchProducts(name, minStock);
    }
}
