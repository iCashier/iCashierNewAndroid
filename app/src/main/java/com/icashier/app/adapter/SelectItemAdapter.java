package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.ItemTodaySpecialBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.SelectItemListener;
import com.icashier.app.model.ExistingItemList;

import java.util.List;

public class SelectItemAdapter extends RecyclerView.Adapter<SelectItemAdapter.ViewHolder>{

    Context context;
    LayoutInflater inflater;
    List<ExistingItemList.ResultBean> itemList;

    public SelectItemAdapter(Context context,List<ExistingItemList.ResultBean> itemList){
        this.context=context;
        this.itemList=itemList;
        this.inflater=LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTodaySpecialBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_today_special,parent,false);
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemTodaySpecialBinding binding;
        public ViewHolder(ItemTodaySpecialBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position)
        {
            binding.tvItemName.setText(itemList.get(position).getName());
            if(itemList.get(position).getSize().equalsIgnoreCase(AppConstant.DEFAULT_SIZE)){
                binding.tvItemSize.setText(context.getString(R.string.default_size));
            }else {
                binding.tvItemSize.setText(itemList.get(position).getSize());
            }
            binding.tvItemPrice.setText(itemList.get(position).getPrice());
            if(Integer.parseInt(itemList.get(position).getQty())<0) {
                binding.tvQuantity.setText(R.string.unlimited);

            }else{
                binding.tvQuantity.setText(itemList.get(position).getQty());
            }

            Utilities.setCategoryImage(context,binding.imgItem, ServerConstants.IMAGE_BASE_URL+itemList.get(position).getImage());

            if(itemList.get(position).isSelected()){
                binding.cbItem.setChecked(true);
            }else{
                binding.cbItem.setChecked(false);
            }
            binding.cbItem.setOnClickListener(V->{
                for(int i=0;i<itemList.size();i++){
                    if(position!=i) {
                        itemList.get(i).setSelected(false);
                    }
                }
                if(itemList.get(position).isSelected()){
                    itemList.get(position).setSelected(false);
                }else {
                    itemList.get(position).setSelected(true);
                }
                notifyDataSetChanged();
            });

        }
    }
}
