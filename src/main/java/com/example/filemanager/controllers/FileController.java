package com.example.filemanager.controllers;

import com.example.filemanager.models.FileModel;
import com.example.filemanager.services.DatabaseService;
import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.scene.control.TextField;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class FileController {
    @FXML private TreeView<String> fileBrowser;
    @FXML private TextField searchField;

    private DatabaseService dbService;

    public void initialize() {
        dbService = new DatabaseService();
        // Initialize file browser
    }

    @FXML
    private void handleSearch() {
        try {
            String query = searchField.getText();
            List<FileModel> results = dbService.searchFiles(query);
            // Display results
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddToDatabase(File file) {
        try {
            FileModel fileModel = new FileModel(file);
            dbService.addFile(fileModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
