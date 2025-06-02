package com.example.filemanager;

import com.example.filemanager.controllers.FileController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/filemanager/hello-view.fxml"));
        BorderPane root = loader.load();

        // Get the controller (now using FileController)
        FileController controller = loader.getController();

        // Initialize the file browser and pass it to the controller
        TreeView<String> fileBrowser = createFileBrowser("C:");
        controller.setDirectoryTree(fileBrowser); // You'll need to add this method to FileController

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("AI File Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TreeView<String> createFileBrowser(String rootPath) {
        TreeItem<String> rootItem = new TreeItem<>(rootPath);
        TreeView<String> treeView = new TreeView<>(rootItem);
        treeView.setStyle("-fx-font-size: 14px; -fx-pref-width: 300px;");

        try {
            File rootFile = new File(rootPath);
            if (rootFile.exists() && rootFile.listFiles() != null) {
                for (File file : rootFile.listFiles()) {
                    rootItem.getChildren().add(createFileTreeItem(file));
                }
            }
        } catch (SecurityException e) {
            System.err.println("Access denied to: " + rootPath);
        }

        rootItem.addEventHandler(TreeItem.<String>branchExpandedEvent(), event -> {
            TreeItem<String> item = event.getTreeItem();
            if (!item.getChildren().isEmpty() &&
                    item.getChildren().get(0).getValue().equals("Loading...")) {

                new Thread(() -> loadDirectoryContents(item)).start();
            }
        });

        return treeView;
    }

    private void loadDirectoryContents(TreeItem<String> parentItem) {
        Platform.runLater(() -> {
            // Clear the "Loading..." placeholder
            parentItem.getChildren().clear();

            File dir = new File(getFullPath(parentItem));
            File[] children = dir.listFiles();

            if (children != null) {
                for (File file : children) {
                    TreeItem<String> newItem = createFileTreeItem(file);
                    parentItem.getChildren().add(newItem);
                }
            }
        });
    }

    private TreeItem<String> createFileTreeItem(File file) {
        TreeItem<String> item = new TreeItem<>(file.getName());

        if (file.isDirectory()) {
            item.getChildren().add(new TreeItem<>("Loading..."));
        }

        return item;
    }

    private String getFullPath(TreeItem<String> item) {
        StringBuilder path = new StringBuilder(item.getValue());
        TreeItem<String> parent = item.getParent();
        while (parent != null) {
            path.insert(0, parent.getValue() + File.separator);
            parent = parent.getParent();
        }
        return path.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}