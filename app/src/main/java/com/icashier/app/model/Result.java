
package com.icashier.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("mid")
    @Expose
    private Integer mid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("icons")
    @Expose
    private Integer icons;
    @SerializedName("arabic_name")
    @Expose
    private String arabicName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIcons() {
        return icons;
    }

    public void setIcons(Integer icons) {
        this.icons = icons;
    }

    public String getArabicName() {
        return arabicName;
    }

    public void setArabicName(String arabicName) {
        this.arabicName = arabicName;
    }

}
