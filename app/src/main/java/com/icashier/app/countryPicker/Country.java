package com.icashier.app.countryPicker;

/**
 * Created by ADMIN on 2/2/2018.
 */

public class Country {
    public String countryCode;
    public String countryName;
    public String dileCode;
    public int flag;
    public String currency;

    public Country(String countryCode, String countryName, String dileCode,int flag,String currency) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.dileCode = dileCode;
        this.flag=flag;
        this.currency=currency;


    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getDileCode() {
        return dileCode;
    }

    public int getFlag(){return flag;}
}
