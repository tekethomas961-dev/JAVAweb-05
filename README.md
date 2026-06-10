# StockManagementSystem_MyBatis

基于 Spring Boot 3、MyBatis、MySQL 8 和 Druid 的商品库存管理系统。项目实现了商品增查、动态查询、出库事务、库存操作日志、Druid 连接池、多环境配置和 MyBatis 二级缓存验证。

## 技术栈

- Java 17
- Spring Boot 3.2.5
- Spring Web
- MyBatis Spring Boot Starter 3.0.3
- MySQL Driver
- Druid 连接池
- Lombok
- JUnit 5 + H2 测试库

## 数据库准备

执行 `src/main/resources/schema.sql` 创建数据库和表：

```sql
CREATE DATABASE IF NOT EXISTS stock_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    stock INT NOT NULL
);

CREATE TABLE stock_log (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    out_quantity INT NOT NULL,
    operate_time DATETIME NOT NULL,
    result VARCHAR(20) NOT NULL
);
```

执行 `src/main/resources/data.sql` 可插入示例商品：苹果、牛奶、面包。

## 配置说明

主配置文件 `src/main/resources/application.yaml` 默认激活开发环境：

```yaml
spring:
  profiles:
    active: dev
```

开发环境配置在 `application-dev.yaml`，预警阈值为 `20`；生产环境配置在 `application-prod.yaml`，预警阈值为 `5`。实际运行前请把数据库用户名和密码改成自己 MySQL 的配置。

Druid 使用 `DruidConfig` 读取 `spring.datasource.druid` 下的连接池参数，配置了初始连接数、最大活跃连接数、验证 SQL、监控页面等。开发环境监控页面地址：

```text
http://localhost:8080/druid/
```

## 运行和打包

测试：

```bash
mvnw.cmd test
```

打包：

```bash
mvnw.cmd clean package
```

开发环境运行：

```bash
java -jar target/StockManagementSystem_MyBatis-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

生产环境运行：

```bash
java -jar target/StockManagementSystem_MyBatis-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## 接口示例

添加商品：

```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"矿泉水\",\"stock\":80}"
```

出库：

```bash
curl -X PUT "http://localhost:8080/products/1/outbound?quantity=10"
```

查询全部：

```bash
curl http://localhost:8080/products
```

动态查询：

```bash
curl "http://localhost:8080/products/search?name=奶&minStock=20"
```

## 关键代码说明

- 实体类：`Product` 使用 Lombok 的 `@Data`、`@AllArgsConstructor`、`@NoArgsConstructor`，并实现 `Serializable`，用于支持二级缓存。
- Mapper：`ProductMapper` 使用注解方式实现 `@Insert`、`@Update`、`@Select`、`@Delete`。`searchProducts` 使用 `<script>`、`<where>`、`<if>` 实现商品名称模糊查询和库存下限的动态组合。
- 事务：`ProductService.outboundProduct` 使用 `@Transactional(rollbackFor = Exception.class)`。出库时先查询库存，不足时抛出 `InsufficientStockException`；库存充足时扣减库存并写入 `stock_log`。如果日志写入后模拟异常，库存更新会一起回滚。
- 二级缓存：`application.yaml` 中设置 `mybatis.configuration.cache-enabled: true`，`ProductMapper` 上添加 `@CacheNamespace`。`ProductMapperCacheTest` 使用同一个 `SqlSessionFactory` 创建两个不同 `SqlSession` 查询同一商品，可在控制台观察第二次查询命中缓存。
- 扩展挑战：已实现 `stock_log` 操作日志表和事务回滚验证；同时实现事务内库存预警，扣减后库存低于 `stock.warn-threshold` 时输出 warn 日志，不阻断事务。

## 单元测试

测试类位于 `src/test/java`：

- `ProductServiceTest`：测试添加商品、出库成功、库存不足异常、动态查询。
- `ProductTransactionRollbackTest`：模拟日志写入后失败，验证库存回滚。
- `ProductMapperCacheTest`：验证 MyBatis 二级缓存跨 `SqlSession` 生效。

测试使用 H2 内存数据库和 `test` profile，不污染本机 MySQL。

## 提交说明

项目源码、`schema.sql`、`data.sql` 和 README 已按作业要求放在 Maven 标准目录中。提交到 Gitee 前建议先执行：

```bash
mvnw.cmd clean test
mvnw.cmd clean package
```
