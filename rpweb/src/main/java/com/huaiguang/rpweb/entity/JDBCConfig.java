package com.huaiguang.rpweb.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class JDBCConfig {

    @Value("${spring.datasource.url}")
    private String JDBC_URL;

    @Value("${spring.datasource.username}")
    private String JDBC_USER;

    @Value("${spring.datasource.password}")
    private String JDBC_PASSWORD;
}
