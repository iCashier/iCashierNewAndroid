package com.icashier.app.model;

import java.io.Serializable;

public class PrinterListModel implements Serializable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    private String name;
    private String target;
}
