package com.example.testcontainer;

import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class MySqlTestContainer {
    private static final String MYSQL_VERSION = "mysql:8";

    @Container
    static JdbcDatabaseContainer mySQLContainer = new MySQLContainer(MYSQL_VERSION)
        .withDatabaseName("test");
}
