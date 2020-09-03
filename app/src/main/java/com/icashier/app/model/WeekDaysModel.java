package com.icashier.app.model;

import java.io.Serializable;

public class WeekDaysModel implements Serializable{

    public WeekDaysModel(String day, boolean isSelected) {
        this.day = day;
        this.isSelected = isSelected;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private String day;
    private boolean isSelected;
}
