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
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.MealChipListener;
import com.icashier.app.model.ExistingItemList;

import java.util.List;

public class MealItemChipAdapter extends RecyclerView.Adapter<MealItemChipAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    List<ExistingItemList.ResultBean> itemList;
    MealChipListener listener;
    boolean showClose;

    public MealItemChipAdapter(Context context,List<ExistingItemList.ResultBean> itemList,MealChipListener listener,boolean showClose){
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.itemList=itemList;
        this.listener=listener;
        this.showClose=showClose;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChipItemMealBinding binding= DataBindingUtil.inflate(inflater, R.layout.chip_item_meal,parent,false);
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

        ChipItemMealBinding binding;
        public ViewHolder(ChipItemMealBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){

            if(itemList.get(position).isSelected()) {
                binding.clParentLayout.setVisibility(View.VISIBLE);
                Utilities.setCategoryImage(context, binding.imgItem, ServerConstants.IMAGE_BASE_URL+itemList.get(position).getImage());
                binding.tvItemName.setText(itemList.get(position).getName());
            }else{
                binding.clParentLayout.setVisibility(View.GONE);
            }

            if(showClose){
                binding.imgDelete.setVisibility(View.VISIBLE);
            }else{
                binding.imgDelete.setVisibility(View.INVISIBLE);
            }
            binding.imgDelete.setOnClickListener(V->{
                itemList.get(position).setSelected(false);
                if(itemList.get(position).getSize().contains(",")) {
                    itemList.get(position).setSelectedSize(itemList.get(position).getSize().split(",")[0]);
                    itemList.get(position).setSelectedPrice(itemList.get(position).getPrice().split(",")[0]);
                }else{
                    itemList.get(position).setSelectedSize(itemList.get(position).getSize());
                    itemList.get(position).setSelectedPrice(itemList.get(position).getPrice());
                }
                notifyDataSetChanged();
                listener.onDeleteClick(position);
            });
        }
    }
}
