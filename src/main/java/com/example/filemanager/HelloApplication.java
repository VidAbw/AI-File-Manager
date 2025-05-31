package com.example.filemanager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.File;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create root layout
        BorderPane root = new BorderPane();

        // Create file browser
        TreeView<String> fileBrowser = createFileBrowser("C:"); // Start from C drive
        root.setCenter(fileBrowser);

        // Set up the scene
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("AI File Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TreeView<String> createFileBrowser(String rootPath) {
        TreeItem<String> rootItem = new TreeItem<>(rootPath);
        TreeView<String> treeView = new TreeView<>(rootItem);

        // Add files and directories to the tree
        File rootFile = new File(rootPath);
        if (rootFile.listFiles() != null) {
            for (File file : rootFile.listFiles()) {
                TreeItem<String> item = new TreeItem<>(file.getName());
                rootItem.getChildren().add(item);

                // Add placeholder for directories to enable expansion
                if (file.isDirectory()) {
                    item.getChildren().add(new TreeItem<>("Loading..."));
                }
            }
        }

        // Handle expansion of directories
        rootItem.addEventHandler(TreeItem.branchExpandedEvent(), event -> {
            TreeItem<String> item = event.getTreeItem();
            if (item.getChildren().size() == 1 &&
                    item.getChildren().get(0).getValue().equals("Loading...")) {
                item.getChildren().clear(); // Remove placeholder
                File dir = new File(getFullPath(item));
                if (dir.listFiles() != null) {
                    for (File file : dir.listFiles()) {
                        TreeItem<String> newItem = new TreeItem<>(file.getName());
                        item.getChildren().add(newItem);

                        if (file.isDirectory()) {
                            newItem.getChildren().add(new TreeItem<>("Loading..."));
                        }
                    }
                }
            }
        });

        return treeView;
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