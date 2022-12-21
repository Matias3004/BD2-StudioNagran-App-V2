package com.studionagranapp.helpers.models;

import java.sql.Date;

public class MixNote {

    private final Integer id;
    private final Date uploadDate;
    private final String description;
    private final Integer mixId;

    public MixNote(Integer id, Date uploadDate, String description, Integer mixId) {
        this.id = id;
        this.uploadDate = uploadDate;
        this.description = description;
        this.mixId = mixId;
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

    public Integer getMixId() {
        return mixId;
    }
}
