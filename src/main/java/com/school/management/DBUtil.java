package com.school.management;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil {
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/school_management");
        config.setUsername("root");
        config.setPassword("Huynguyen29");

        config.setMaximumPoolSize(10);           // Số lượng kết nối tối đa
        config.setMinimumIdle(2);                // Kết nối idle tối thiểu
        config.setIdleTimeout(30000);            // 30s
        config.setConnectionTimeout(30000);      // 30s
        config.setMaxLifetime(600000);           // 10 phút

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection(); // Lấy connection từ pool
    }

    public static void closePool() {
        dataSource.close(); // Đóng pool khi ứng dụng kết thúc
    }
}
