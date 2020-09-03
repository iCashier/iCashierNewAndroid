package com.icashier.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.icashier.app.R;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CheckBoxListener;
import com.icashier.app.listener.MealListListener;

import com.icashier.app.model.MealListResponse;

import java.util.List;

public class SelectMealAdapter extends BaseExpandableListAdapter {

    Context context;
    boolean isExpanded;
    List<MealListResponse.ResultBean> mealList;
    boolean isMultiSelect;
    CheckBoxListener listener;

    public SelectMealAdapter(Context context, List<MealListResponse.ResultBean> mealList, boolean isMultiSelect, CheckBoxListener listener){
        this.context=context;
        this.mealList=mealList;
        this.isMultiSelect=isMultiSelect;
        this.listener=listener;

    }

    @Override
    public int getGroupCount() {
        return mealList.size();
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return mealList.get(groupPosition).getItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView=layoutInflater.inflate(R.layout.item_meal_primary,null);


        ImageView imgPlusMinus=convertView.findViewById(R.id.imgPlusMinus);
        TextView tvTitle=convertView.findViewById(R.id.tvTittle);
        CheckBox checkBox=convertView.findViewById(R.id.cbMeal);
        ImageView imgDelete=convertView.findViewById(R.id.imgDelete);
        ImageView imgEdit=convertView.findViewById(R.id.imgEdit);

        checkBox.setVisibility(View.VISIBLE);

        imgDelete.setVisibility(View.INVISIBLE);
        imgEdit.setVisibility(View.INVISIBLE);


        tvTitle.setText(mealList.get(groupPosition).getTitle());

        if(mealList.get(groupPosition).getItems().size()>0) {
            if (mealList.get(groupPosition).isExpanded()) {
                imgPlusMinus.setSelected(true);
            } else {
                imgPlusMinus.setSelected(false);
            }
        }

        imgPlusMinus.setOnClickListener(V->{
            if(isExpanded) {
                mealList.get(groupPosition).setExpanded(false);
                ((ExpandableListView) parent).collapseGroup(groupPosition);
            }
            else {
                mealList.get(groupPosition).setExpanded(true);
                ((ExpandableListView) parent).expandGroup(groupPosition, true);
            }
            notifyDataSetChanged();
        });

        if(isMultiSelect) {
            if (mealList.get(groupPosition).isSelected() || mealList.get(groupPosition).getSpecialitem() == 1) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        }else{
            if (mealList.get(groupPosition).isSelected()) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        }

        checkBox.setOnClickListener(V->{
            if(mealList.get(groupPosition).isSelected()){
                mealList.get(groupPosition).setSelected(false);
            }else{
                if(!isMultiSelect) {
                    for (int i = 0; i < mealList.size(); i++) {
                        mealList.get(i).setSelected(false);
                    }
                }
                mealList.get(groupPosition).setSelected(true);
            }
            listener.onCheckChanged(groupPosition);

            notifyDataSetChanged();
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_meal_secondary, null);
        }

        ImageView imgItem=convertView.findViewById(R.id.imgItem);
        TextView tvItemName=convertView.findViewById(R.id.tvItemName);



        tvItemName.setText(mealList.get(groupPosition).getItems().get(childPosition).getName());
        Utilities.setCategoryImage(context,imgItem,mealList.get(groupPosition).getItems().get(childPosition).getImage());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
