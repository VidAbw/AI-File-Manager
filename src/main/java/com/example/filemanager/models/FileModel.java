package com.example.filemanager.models;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class FileModel {
    private String Id;
    private String OriginalName;
    private String displayName;
    private String filePath;
    private String fileType;
    private long size;
    private Date CreatedAt;
    private Date modifiedAt;
    private boolean isDirectory;
    private boolean isHidden;
    private String tags;
    private String thumbnailPath;
    private transient File fileReference;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy HH:mm");

    // Constructors
    public FileModel() {
        this.Id = UUID.randomUUID().toString();
        this.CreatedAt = new Date();
    }

    public FileModel(File file) {
        this();
        this.OriginalName = file.getName();
        this.displayName = file.getName();
        this.filePath = file.getAbsolutePath();
        this.fileType = getFileExtension(file);
        this.size = file.length();
        this.modifiedAt = new Date(file.lastModified());
        this.isDirectory = file.isDirectory();
        this.isHidden = file.isHidden();
        this.fileReference = file;
    }

    // Getters and Setters (unchanged from your version)
    // ...

    // Enhanced Utility Methods
    private String getFileExtension(File file) {
        if (file.isDirectory()) {
            return "Folder";
        }

        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        return (lastDot == -1 || lastDot == 0) ? "File"
                : name.substring(lastDot + 1).toUpperCase();
    }

    public String getFormattedSize() {
        if (isDirectory) return "";

        if (size < 1024) return size + " B";
        int exp = (int) (Math.log(size) / Math.log(1024));
        char unit = "KMGTPE".charAt(exp-1);
        return String.format("%.1f %sB", size / Math.pow(1024, exp), unit);
    }

    public String getFormattedModifiedDate() {
        return modifiedAt.toString(); // Or use SimpleDateFormat for better formatting
    }

    // Additional useful methods
    public boolean isImageFile() {
        if (isDirectory) return false;
        String[] imageExtensions = {"JPG", "JPEG", "PNG", "GIF", "BMP", "WEBP"};
        for (String ext : imageExtensions) {
            if (ext.equalsIgnoreCase(fileType)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDocumentFile() {
        if (isDirectory) return false;
        String[] docExtensions = {"PDF", "DOC", "DOCX", "TXT", "RTF", "ODT"};
        for (String ext : docExtensions) {
            if (ext.equalsIgnoreCase(fileType)) {
                return true;
            }
        }
        return false;
    }

    // Improved equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileModel)) return false;
        FileModel fileModel = (FileModel) o;
        return Objects.equals(filePath, fileModel.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath);
    }

    public File getFileReference() {
        return fileReference;
    }

    public void setFileReference(File fileReference) {
        this.fileReference = fileReference;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        CreatedAt = createdAt;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getOriginalName() {
        return OriginalName;
    }

    public void setOriginalName(String originalName) {
        OriginalName = originalName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", displayName,
                isDirectory ? "Folder" : fileType);
    }


}