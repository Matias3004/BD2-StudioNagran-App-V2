package com.studionagranapp.helpers.models;

public class Equipment {

    private final Integer id;
    private final String name;
    private final String type;
    private final Integer quantity;
    private final Boolean backline;

    public Equipment(Integer id, String name, String type, Integer quantity, Boolean backline) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.backline = backline;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Boolean getBackline() {
        return backline;
    }
}
