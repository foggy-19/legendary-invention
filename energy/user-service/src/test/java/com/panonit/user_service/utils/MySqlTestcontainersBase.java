package com.panonit.user_service.utils;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.mysql.MySQLContainer;

public abstract class MySqlTestcontainersBase {

    @ServiceConnection
    @Container
    static MySQLContainer mysql = new MySQLContainer("mysql:8.4")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");
}
