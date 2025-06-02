package com.example.filemanager.services;

import com.example.filemanager.models.FileModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private static final String URL = "jdbc:mysql://localhost:3306/filemanager_db";
    private static final String USER = "root";
    private static final String PASSWORD = "dbms123";

    private Connection connection; // Add this field declaration

    // ... [previous connection code]

    public void addFile(FileModel file) throws SQLException {
        String sql = "INSERT INTO files (id, original_name, system_name, file_path, file_type, size, created_at, modified_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, file.getId());
            statement.setString(2, file.getOriginalName());
            statement.setString(3, file.getDisplayName());
            statement.setString(4, file.getFilePath());
            statement.setString(5, file.getFileType());
            statement.setLong(6, file.getSize());
            statement.setTimestamp(7, new Timestamp(file.getCreatedAt().getTime()));
            statement.setTimestamp(8, new Timestamp(file.getModifiedAt().getTime()));

            statement.executeUpdate();
        }
    }

    public List<FileModel> searchFiles(String query) throws SQLException {
        List<FileModel> results = new ArrayList<>();
        String sql = "SELECT * FROM files WHERE original_name LIKE ? OR file_type LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                FileModel file = new FileModel();
                file.setId(rs.getString("id"));
                file.setOriginalName(rs.getString("original_name"));
                file.setFilePath(rs.getString("file_path"));
                // Set other fields...

                results.add(file);
            }
        }

        return results;
    }
}