package com.icashier.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.ItemExistingItemBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.ExistingItemListener;
import com.icashier.app.model.ExistingItemList;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExistingItemAdapter extends RecyclerView.Adapter<ExistingItemAdapter.ViewHolder> {

    Context context;
    List<ExistingItemList.ResultBean> existingItemList;
    LayoutInflater inflater;
    ExistingItemListener listener;
    public ExistingItemAdapter(Context context,List<ExistingItemList.ResultBean> existingItemList,ExistingItemListener listener){
        this.existingItemList=existingItemList;
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.listener=listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExistingItemBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_existing_item,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return existingItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ItemExistingItemBinding binding;
        public ViewHolder(ItemExistingItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        void bindData(int position){

            binding.tvItemName.setText(existingItemList.get(position).getName());




            binding.tvItemSize.setText(existingItemList.get(position).getSize().replace(",",", ").replace(AppConstant.DEFAULT_SIZE,context.getString(R.string.default_size)));
            binding.tvItemPrice.setText(existingItemList.get(position).getPrice().replace(",",", "));

            if(Integer.parseInt(existingItemList.get(position).getQty())<0) {
                binding.tvQuantity.setText(R.string.unlimited);

            }else{
                binding.tvQuantity.setText(existingItemList.get(position).getQty());
            }

            Utilities.setCategoryImage(context,binding.imgItem, ServerConstants.IMAGE_BASE_URL+existingItemList.get(position).getImage());

            binding.imgEdit.setOnClickListener(V->{
                listener.onEditClick(position);
            });

            binding.imgDelete.setOnClickListener(V->{
                listener.onDeleteClick(position);
            });
        }
    }
}
