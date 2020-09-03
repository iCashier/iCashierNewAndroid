package com.icashier.app.model;

import java.io.Serializable;

public class AddressModel implements Serializable {


    public AddressModel(String name, String address, String landmark, String city, String country, String phoneNumber, String code, int flag) {
        this.name = name;
        this.address = address;
        this.landmark = landmark;
        this.city = city;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.flag = flag;
    }

    private String name;
    private String address;
    private String landmark;
    private String city;
    private String country;
    private String phoneNumber;
    private String code;
    private int flag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getCode() {
        return code;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
