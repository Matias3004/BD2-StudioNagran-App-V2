package com.studionagranapp.helpers.models;

import java.sql.Date;

public class MixNote {

    private final Integer id;
    private final Date uploadDate;
    private final String description;
    private final String filename;
    private final String sessionName;
    private final String bandName;

    public MixNote(Integer id, Date uploadDate, String description, String filename, String sessionName, String bandName) {
        this.id = id;
        this.uploadDate = uploadDate;
        this.description = description;
        this.filename = filename;
        this.sessionName = sessionName;
        this.bandName = bandName;
    }

    public Integer getId() {
        return id;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public String getDescription() {
        return description;
    }

    public String getFilename() {
        return filename;
    }

    public String getSessionName() {
        return sessionName;
    }

    public String getBandName() {
        return bandName;
    }
}
