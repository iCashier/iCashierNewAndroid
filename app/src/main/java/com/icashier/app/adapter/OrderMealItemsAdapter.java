package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.ChipItemMealBinding;
import com.icashier.app.databinding.ItemItemInMealBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.MealChipListener;
import com.icashier.app.model.ExistingItemList;
import com.icashier.app.model.MenuResponse;
import com.icashier.app.model.OrderListResponse;

import java.util.List;

public class OrderMealItemsAdapter extends RecyclerView.Adapter<OrderMealItemsAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    List<OrderListResponse.ResultBean.ItemsBean> itemList;

    public OrderMealItemsAdapter(Context context,List<OrderListResponse.ResultBean.ItemsBean> itemList){
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.itemList=itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemItemInMealBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_item_in_meal,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ItemItemInMealBinding binding;
        public ViewHolder(ItemItemInMealBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){

            Utilities.setCategoryImage(context, binding.imgItem, itemList.get(position).getImage());
            binding.tvItemName.setText(itemList.get(position).getName());
            binding.rvAllergies.setVisibility(View.GONE);
            binding.tvKCal.setVisibility(View.GONE);

        }
    }
}
