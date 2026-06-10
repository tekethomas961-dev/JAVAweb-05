DROP TABLE IF EXISTS stock_log;
DROP TABLE IF EXISTS product;

CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    stock INT NOT NULL
);

CREATE TABLE stock_log (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    out_quantity INT NOT NULL,
    operate_time TIMESTAMP NOT NULL,
    result VARCHAR(20) NOT NULL,
    CONSTRAINT fk_stock_log_product FOREIGN KEY (product_id) REFERENCES product(id)
);
