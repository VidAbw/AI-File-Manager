package com.example.filemanager.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {
    private static final String URL = "root@localhost:3306";
    private static final String USER = "root";
    private static final String PASSWORD = "dbms123";

    private Connection connection;

    public DatabaseService() {
        try{
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            initializeDatabase();
        }catch (SQLException e){
            System.err.println("Database connection failed: "+e.getMessage());
        }
    }

    private void initializeDatabase() throws SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS files (" +
                "id VARCHAR(36) PRIMARY KEY," +
                "original_name VARCHAR(255)," +
                "system_name VARCHAR(255)," +
                "file_path TEXT," +
                "file_type VARCHAR(50)," +
                "size BIGINT," +
                "created_at TIMESTAMP," +
                "modified_at TIMESTAMP," +
                "owner_id VARCHAR(36)" +
                ")";
        connection.createStatement().execute(sql);
    }
}
