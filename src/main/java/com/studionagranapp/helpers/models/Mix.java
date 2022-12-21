package com.studionagranapp.helpers.models;

import java.sql.Date;

public class Mix {

    private final Integer id;
    private final String filename;
    private final Date uploadDate;
    private final String path;
    private final Integer sessionId;

    public Mix(Integer id, String filename, Date uploadDate, String path, Integer sessionId) {
        this.id = id;
        this.filename = filename;
        this.uploadDate = uploadDate;
        this.path = path;
        this.sessionId = sessionId;
    }

    public Integer getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public String getPath() {
        return path;
    }

    public Integer getSessionId() {
        return sessionId;
    }
}
