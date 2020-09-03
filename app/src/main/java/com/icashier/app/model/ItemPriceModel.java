package com.icashier.app.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class ItemPriceModel implements Serializable,Comparable<ItemPriceModel> {

    private String size;
    private String price;

    public ItemPriceModel(String size, String price) {
        this.size = size;
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    @Override
    public int compareTo(@NonNull ItemPriceModel o) {
        return this.price.compareTo(o.price);

    }
}
