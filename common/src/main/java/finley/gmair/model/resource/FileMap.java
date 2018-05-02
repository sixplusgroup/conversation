package finley.gmair.model.resource;

import finley.gmair.model.Entity;

public class FileMap extends Entity {
    private String fileId;
    private String fileUrl;
    private String actualPath;
    private String fileName;
    public FileMap(){super();}
    public FileMap(String fileUrl, String actualPath, String fileName){
        this();
        this.fileUrl = fileUrl;
        this.actualPath = actualPath;
        this.fileName = fileName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getActualPath() {
        return actualPath;
    }

    public void setActualPath(String actualPath) {
        this.actualPath = actualPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
