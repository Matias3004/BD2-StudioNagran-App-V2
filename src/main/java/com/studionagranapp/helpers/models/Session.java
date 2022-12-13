package com.studionagranapp.helpers.models;

import java.sql.Date;

public class Session {

    private final Integer id;
    private final String name;
    private final String bandName;
    private final Date startDate;
    private final Date endDate;
    private final Integer duration;
    private final Integer clientID;
    private final Integer engineerID;

    public Session(Integer id, String name, String bandName, Date startDate, Date endDate, Integer duration, Integer clientID, Integer engineerID) {
        this.id = id;
        this.name = name;
        this.bandName = bandName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.clientID = clientID;
        this.engineerID = engineerID;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBandName() {
        return bandName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getClientID() {
        return clientID;
    }

    public Integer getEngineerID() {
        return engineerID;
    }
}
