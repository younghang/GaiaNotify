package com.young.gaianotify.FileDeal;

/**
 * Created by young on 2016/5/7 0007.
 */
public class ThreadInfo {
    int id;
    String url;
    int start;
    int end;
    int progress;

    public ThreadInfo(int id, String url, int start, int end, int progress) {
        this.id = id;
        this.url = url;
        this.start = start;
        this.end = end;
        this.progress = progress;
    }

    public ThreadInfo(String url, int id) {
        this.url = url;
        this.id = id;
    }

    public ThreadInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
