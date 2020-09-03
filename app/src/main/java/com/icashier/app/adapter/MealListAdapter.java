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
import com.icashier.app.listener.MealListListener;
import com.icashier.app.listener.PrimaryCategoryListener;
import com.icashier.app.model.CategoryListReponse;
import com.icashier.app.model.MealListResponse;

import java.util.List;

public class MealListAdapter extends BaseExpandableListAdapter {

    Context context;
    boolean isExpanded;
    List<MealListResponse.ResultBean> mealList;
    MealListListener listener;

    public MealListAdapter(Context context, List<MealListResponse.ResultBean> mealList,MealListListener listener){
        this.context=context;
        this.mealList=mealList;
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
        ImageView imgDelete=convertView.findViewById(R.id.imgDelete);
        ImageView imgEdit=convertView.findViewById(R.id.imgEdit);
        CheckBox checkBox=convertView.findViewById(R.id.cbMeal);

        checkBox.setVisibility(View.GONE);

        tvTitle.setText(mealList.get(groupPosition).getTitle());

        if(mealList.get(groupPosition).getItems().size()>0) {
            if (mealList.get(groupPosition).isExpanded()) {
                imgPlusMinus.setSelected(true);
            } else {
                imgPlusMinus.setSelected(false);
            }
        }
        imgDelete.setOnClickListener(V->{
            listener.onDeleteClick(groupPosition);
        });

        imgEdit.setOnClickListener(V->{
            listener.onEditClick(groupPosition);
        });
        imgPlusMinus.setOnClickListener(V->{
            listener.onExpanded(groupPosition);
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
