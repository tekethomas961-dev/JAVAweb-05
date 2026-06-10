USE stock_db;

INSERT INTO product(name, stock)
SELECT '苹果', 100 WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = '苹果');
INSERT INTO product(name, stock)
SELECT '牛奶', 35 WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = '牛奶');
INSERT INTO product(name, stock)
SELECT '面包', 12 WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = '面包');
