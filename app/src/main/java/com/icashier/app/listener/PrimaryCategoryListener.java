package com.icashier.app.listener;

public interface PrimaryCategoryListener {

    void onDeleteClick(int position);
    void onEditClick(int position);
    void onEditClick(int groupPosition,int childPosition);
    void onDeleteClick(int groupPositon,int childPosition);
    void onExpanded(int groupPosition);

}
