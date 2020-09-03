package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icashier.app.R;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.ItemPrimaryCategroyBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.PrimaryCategoryListener;
import com.icashier.app.model.CategoryListReponse;

import java.util.List;
import java.util.Locale;

public class CategoryExpandableAdapter extends BaseExpandableListAdapter {

    Context context;
    boolean isExpanded;
    List<CategoryListReponse.ResultBean> categoryList;
    PrimaryCategoryListener primaryCategoryListener;

    public CategoryExpandableAdapter(Context context,List<CategoryListReponse.ResultBean> categoryList,PrimaryCategoryListener primaryCategoryListener){
        this.context=context;
        this.categoryList=categoryList;
        this.primaryCategoryListener=primaryCategoryListener;

    }

    @Override
    public int getGroupCount() {
        return categoryList.size();
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return categoryList.get(groupPosition).getSubCategories().size();
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
        convertView=layoutInflater.inflate(R.layout.item_primary_categroy,null);


        ImageView imgPlusMinus=convertView.findViewById(R.id.imgPlusMinus);
        TextView tvPrimaryCategory=convertView.findViewById(R.id.tvPrimaryCategroy);
        ImageView imgCategory=convertView.findViewById(R.id.imgCategory);
        ImageView imgDelete=convertView.findViewById(R.id.imgDeletePrimary);
        ImageView imgEdit=convertView.findViewById(R.id.imgEditPrimary);

        if (Locale.getDefault().equals(new Locale("ar","MA"))) {
            if(categoryList.get(groupPosition).getArabic_name()!=null&&!categoryList.get(groupPosition).getArabic_name().equalsIgnoreCase("")) {
                tvPrimaryCategory.setText(categoryList.get(groupPosition).getArabic_name());
            }else{
                tvPrimaryCategory.setText(categoryList.get(groupPosition).getName());
            }
        }else{
            tvPrimaryCategory.setText(categoryList.get(groupPosition).getName());

        }
        Utilities.setCategoryImage(context,imgCategory, ServerConstants.IMAGE_BASE_URL+categoryList.get(groupPosition).getImage());



        if(categoryList.get(groupPosition).getSubCategories().size()>0) {
            if(((ExpandableListView)parent).isGroupExpanded(groupPosition)){
                imgPlusMinus.setSelected(true);
            }else{
                imgPlusMinus.setSelected(false);
            }

            /*if (categoryList.get(groupPosition).isExpanded()) {

                imgPlusMinus.setSelected(true);
            } else {

                imgPlusMinus.setSelected(false);
            }*/
        }
        imgDelete.setOnClickListener(V->{
            primaryCategoryListener.onDeleteClick(groupPosition);
        });

        imgEdit.setOnClickListener(V->{
            primaryCategoryListener.onEditClick(groupPosition);
        });
        imgPlusMinus.setOnClickListener(V->{
            if(categoryList.get(groupPosition).getSubCategories().size()>1) {
                primaryCategoryListener.onExpanded(groupPosition);
                if (isExpanded) {
                    categoryList.get(groupPosition).setExpanded(false);
                    ((ExpandableListView) parent).collapseGroup(groupPosition);

                } else {
                    categoryList.get(groupPosition).setExpanded(true);
                    ((ExpandableListView) parent).expandGroup(groupPosition, true);
                }
            }
            notifyDataSetChanged();
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_secondary_category, null);
        }

        if(categoryList.get(groupPosition).getSubCategories().get(childPosition).getName().equalsIgnoreCase("others")){
            convertView.findViewById(R.id.clParentLayout).setVisibility(View.GONE);
        }else{
            convertView.findViewById(R.id.clParentLayout).setVisibility(View.VISIBLE);
        }

        ImageView imgCategory=convertView.findViewById(R.id.imgCategory);
        TextView tvSubCatName=convertView.findViewById(R.id.tvSecondarCategroy);
        ImageView imgEdit=convertView.findViewById(R.id.imgEditSecondary);
        ImageView imgDelete=convertView.findViewById(R.id.imgDeleteSecondary);

        imgEdit.setOnClickListener(V->{
            primaryCategoryListener.onEditClick(groupPosition,childPosition);
        });

        imgDelete.setOnClickListener(V->{
            primaryCategoryListener.onDeleteClick(groupPosition,childPosition);
        });



        tvSubCatName.setText(categoryList.get(groupPosition).getSubCategories().get(childPosition).getName());
        Utilities.setCategoryImage(context,imgCategory,ServerConstants.IMAGE_BASE_URL+categoryList.get(groupPosition).getSubCategories().get(childPosition).getImage());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
