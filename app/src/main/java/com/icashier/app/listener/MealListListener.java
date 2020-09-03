package com.icashier.app.listener;

public interface MealListListener {
    void onDeleteClick(int position);
    void onEditClick(int position);
    void onExpanded(int position);
}
