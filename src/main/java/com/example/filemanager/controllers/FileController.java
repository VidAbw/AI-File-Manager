package com.example.filemanager.controllers;

import com.example.filemanager.models.FileModel;
import com.example.filemanager.services.DatabaseService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class FileController {
    @FXML private TreeView<String> directoryTree;
    @FXML private TableView<FileModel> filesTable;
    @FXML private TableColumn<FileModel, String> nameColumn;
    @FXML private TableColumn<FileModel, String> typeColumn;
    @FXML private TableColumn<FileModel, String> sizeColumn;
    @FXML private TableColumn<FileModel, String> modifiedColumn;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;

    private DatabaseService dbService;
    private ObservableList<FileModel> fileList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        dbService = new DatabaseService();
        setupTableColumns();
        initializeDirectoryTree();
    }

    public void setDirectoryTree(TreeView<String> directoryTree) {
        this.directoryTree = directoryTree;
    }

    private void setupTableColumns() {
        // Configure table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("fileType"));

        // Custom formatters
        sizeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFormattedSize()));

        modifiedColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFormattedModifiedDate()));

        filesTable.setItems(fileList);
    }

    private void initializeDirectoryTree() {
        TreeItem<String> rootItem = new TreeItem<>("Computer");
        directoryTree.setRoot(rootItem);

        // Add some drives (Windows example)
        rootItem.getChildren().add(new TreeItem<>("C:"));
        rootItem.getChildren().add(new TreeItem<>("D:"));

        // Set up directory expansion handler
        directoryTree.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadFilesForDirectory(newVal.getValue());
            }
        });
    }

    private void loadFilesForDirectory(String path) {
        fileList.clear();
        File dir = new File(path);

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    fileList.add(new FileModel(file));
                }
            }
        }
    }

    @FXML
    private void handleSearch() {
        try {
            String query = searchField.getText();
            List<FileModel> results = dbService.searchFiles(query);
            fileList.setAll(results);
            statusLabel.setText("Found " + results.size() + " files");
        } catch (SQLException e) {
            statusLabel.setText("Search error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        TreeItem<String> selected = directoryTree.getSelectionModel().getSelectedItem();
        if (selected != null) {
            loadFilesForDirectory(selected.getValue());
            statusLabel.setText("Refreshed directory");
        }
    }

    @FXML
    private void handleHome() {
        directoryTree.getSelectionModel().select(0); // Select root
        statusLabel.setText("Returned to home");
    }

    @FXML
    private void handleAddToDatabase(File file) {
        try {
            FileModel fileModel = new FileModel(file);
            dbService.addFile(fileModel);
            statusLabel.setText("Added to database: " + file.getName());
        } catch (SQLException e) {
            statusLabel.setText("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}