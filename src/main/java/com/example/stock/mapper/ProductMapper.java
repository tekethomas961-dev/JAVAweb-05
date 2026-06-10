package com.example.stock.mapper;

import com.example.stock.domain.Product;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
@CacheNamespace
public interface ProductMapper {

    @Insert("INSERT INTO product(name, stock) VALUES(#{name}, #{stock})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Product product);

    @Update("UPDATE product SET stock = #{stock} WHERE id = #{id}")
    int updateStock(@Param("id") Integer id, @Param("stock") Integer stock);

    @Select("SELECT id, name, stock FROM product WHERE id = #{id}")
    Product findById(Integer id);

    @Select("SELECT id, name, stock FROM product ORDER BY id")
    List<Product> findAll();

    @Delete("DELETE FROM product WHERE id = #{id}")
    int deleteById(Integer id);

    @Select("""
            <script>
            SELECT id, name, stock
            FROM product
            <where>
                <if test="name != null and name != ''">
                    AND name LIKE CONCAT('%', #{name}, '%')
                </if>
                <if test="minStock != null">
                    AND stock &gt;= #{minStock}
                </if>
            </where>
            ORDER BY id
            </script>
            """)
    List<Product> searchProducts(@Param("name") String name, @Param("minStock") Integer minStock);
}
