package com.young.gaianotify.FileDeal;

import java.io.Serializable;

/**
 * Created by young on 2016/5/7 0007.
 */
public class FileInfo implements Serializable {
    int id;
    int progress;
    int length;
    String fileName;
    String url;

    public String getFileAbsolutePath() {
        return fileAbsolutePath;
    }

    public void setFileAbsolutePath(String fileAbsolutePath) {
        this.fileAbsolutePath = fileAbsolutePath;
    }

    String fileAbsolutePath;
    public FileInfo(String url, String fileName, int id, int progress, int length) {
        this.url = url;
        this.fileName = fileName;
        this.id = id;
        this.progress = progress;
        this.length = length;
    }

    public FileInfo(String fileName, String url, int progress) {
        this.fileName = fileName;
        this.url = url;
        this.progress = progress;
    }

    public FileInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id=" + id +
                ", progress=" + progress +
                ", length=" + length +
                ", fileName='" + fileName + '\'' +
                ", fileAbsolutePath='" + fileAbsolutePath + '\'' +
                '}';
    }
}
