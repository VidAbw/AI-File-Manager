package com.example.filemanager;

import com.example.filemanager.controllers.HelloController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        BorderPane root = loader.load();

        // Get controller and pass components if needed
        HelloController controller = loader.getController();

        TreeView<String> fileBrowser = createFileBrowser("C:");
        root.setCenter(fileBrowser);

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
            if (item.getChildren().isEmpty()  &&
                    item.getChildren().get(0).getValue().equals("Loading...")) {

                //clear the placeholder
                item.getChildren().clear();

                //Load directory contents in background thread
                new Thread(() -> {
                    File dir = new File(getFullPath(item));
                    File[] children = dir.listFiles();

                    if (children != null) {
                        Platform.runLater(()->{
                            for (File file : children) {
                                TreeItem<String> newItem = new TreeItem<>(file.getName());
                                item.getChildren().add(newItem);

                                if (file.isDirectory()) {
                                    newItem.getChildren().add(new TreeItem<>("Loading..."));
                                }
                            }
                        });
                    }

                }).start();
            }
        });

        return treeView;
    }

    private void loadDirectoryContents(TreeItem<String> parentItem) {
        Platform.runLater(() -> parentItem.getChildren().clear());

        File dir = new File(getFullPath(parentItem));
        if (dir.listFiles() != null) {
            for (File file : dir.listFiles()) {
                Platform.runLater(() ->
                        parentItem.getChildren().add(createFileTreeItem(file))
                );
            }
        }
    }

    private TreeItem<String> createFileTreeItem(File file) {
        TreeItem<String> item = new TreeItem<>(file.getName());

        // Set icon based on file type
        // item.setGraphic(getIconForFile(file));

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