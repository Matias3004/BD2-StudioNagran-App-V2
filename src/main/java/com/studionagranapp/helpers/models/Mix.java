package com.studionagranapp.helpers.models;

import java.sql.Date;

public class Mix {

    private final Integer id;
    private final String filename;
    private final Date uploadDate;
    private final String path;
    private final String sessionName;
    private final String bandName;

    public Mix(Integer id, String filename, Date uploadDate, String path, String sessionName, String bandName) {
        this.id = id;
        this.filename = filename;
        this.uploadDate = uploadDate;
        this.path = path;
        this.sessionName = sessionName;
        this.bandName = bandName;
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

    public String getSessionName() {
        return sessionName;
    }

    public String getBandName() {
        return bandName;
    }
}
