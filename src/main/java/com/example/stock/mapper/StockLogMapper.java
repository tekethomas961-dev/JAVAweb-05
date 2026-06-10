package com.example.stock.mapper;

import com.example.stock.domain.StockLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface StockLogMapper {

    @Insert("""
            INSERT INTO stock_log(product_id, out_quantity, operate_time, result)
            VALUES(#{productId}, #{outQuantity}, #{operateTime}, #{result})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "logId")
    int insert(StockLog stockLog);
}
