package com.icashier.app.listener;

public interface MenuListener {
    void onCategoryClick(int position);
    void onItemSelected(int position);
    void onStockChanged(int position);
}
