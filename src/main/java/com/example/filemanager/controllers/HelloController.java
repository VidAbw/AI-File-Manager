package com.example.filemanager.controllers;

import com.example.filemanager.models.FileModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;


public class HelloController {
    @FXML private BorderPane mainPane;
    @FXML private TreeView<String> directoryTree;
    @FXML private TableView<FileModel> filesTable;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        // Initialization code will go here
        statusLabel.setText("Welcome to AI File Manager");

        // Set up table columns (we'll implement this fully later)
        setupTableColumns();

        // Initialize directory tree
        initializeDirectoryTree();
    }

    private void setupTableColumns() {
        // We'll implement proper cell value factories later
    }

    private void initializeDirectoryTree() {
        // Basic tree structure - we'll enhance this later
        TreeItem<String> rootItem = new TreeItem<>("Computer");
        directoryTree.setRoot(rootItem);

        // Add some dummy items for now
        TreeItem<String> cDrive = new TreeItem<>("C:");
        rootItem.getChildren().add(cDrive);
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        statusLabel.setText("Searching for: " + query);
        // Actual search implementation will go here
    }

    @FXML
    private void handleRefresh() {
        statusLabel.setText("Refreshing...");
        // Refresh implementation will go here
    }

    @FXML
    private void handleHome() {
        statusLabel.setText("Returning to home directory");
        // Home implementation will go here
    }

    @FXML
    private void handleSettings() {
        statusLabel.setText("Opening settings");
        // Settings implementation will go here
    }
}