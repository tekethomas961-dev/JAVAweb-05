package com.example.stock.mapper;

import com.example.stock.domain.Product;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ProductMapperCacheTest {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    void secondLevelCacheCanBeHitAcrossDifferentSqlSessions() {
        Product first;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            first = session.selectOne("com.example.stock.mapper.ProductMapper.findById", 1);
            session.commit();
        }

        Product second;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            second = session.selectOne("com.example.stock.mapper.ProductMapper.findById", 1);
        }

        assertThat(second).isEqualTo(first);
    }
}
