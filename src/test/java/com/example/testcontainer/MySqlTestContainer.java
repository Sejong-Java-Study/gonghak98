package com.example.testcontainer;

import com.example.gimmegonghakauth.dao.GonghakRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Component
public class MySqlTestContainer {
    private static final String MYSQL_VERSION = "mysql:8";

    @Autowired
    private GonghakRepository gonghakRepository;

    @Container
    static JdbcDatabaseContainer mySQLContainer = new MySQLContainer(MYSQL_VERSION)
        .withDatabaseName("gonghak-auth-for-test")
        .withInitScript("testcontainers/schema.sql");
}
