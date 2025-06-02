package com.example.filemanager.models;

import java.io.File;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class FileModel {
    private String id;
    private String originalName;
    private String displayName;
    private String filePath;
    private String fileType;
    private long size;
    private Date createdAt;
    private Date modifiedAt;
    private boolean isDirectory;
    private String tags;
    private String thumbnailPath;
    private transient File fileReference; // Transient to exclude from serialization

    // Constructors
    public FileModel() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = new Date();
    }

    public FileModel(File file) {
        this();
        this.originalName = file.getName();
        this.displayName = file.getName();
        this.filePath = file.getAbsolutePath();
        this.fileType = getFileExtension(file);
        this.size = file.length();
        this.modifiedAt = new Date(file.lastModified());
        this.isDirectory = file.isDirectory();
        this.fileReference = file;
    }

    // Enhanced getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public File getFileReference() {
        if (fileReference == null && filePath != null) {
            fileReference = new File(filePath);
        }
        return fileReference;
    }

    // Utility methods
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf('.');
        if (lastIndexOf == -1 || lastIndexOf == 0) {
            return isDirectory ? "Folder" : "File";
        }
        return name.substring(lastIndexOf + 1).toUpperCase();
    }

    public String getFormattedSize() {
        if (isDirectory) return "";

        if (size < 1024) return size + " B";
        int exp = (int) (Math.log(size) / Math.log(1024));
        char unit = "KMGTPE".charAt(exp-1);
        return String.format("%.1f %sB", size / Math.pow(1024, exp), unit);
    }

    public String getFormattedModifiedDate() {
        return modifiedAt.toString(); // Could use SimpleDateFormat for better formatting
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileModel fileModel = (FileModel) o;
        return Objects.equals(id, fileModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return displayName;
    }
}