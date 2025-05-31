package com.example.filemanager.models;

import java.io.File;
import java.util.Date;

public class FileModel {
    private String id;
    private String originalName;
    private String systemName;
    private String filePath;
    private String fileType;
    private long size;
    private Date createdAt;
    private Date modifiedAt;

    // Constructors
    public FileModel() {}

    public FileModel(File file) {
        this.originalName = file.getName();
        this.systemName = file.getName();
        this.filePath = file.getAbsolutePath();
        this.fileType = getFileExtension(file);
        this.size = file.length();
        this.modifiedAt = new Date(file.lastModified());
    }

    // Getters and setters
    // ...

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf + 1).toLowerCase();
    }
}