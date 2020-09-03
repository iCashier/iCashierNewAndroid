package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemsInInvoiceBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.OrderListResponse;

import java.util.List;

public class ItemsInInvoiceAdapter extends RecyclerView.Adapter<ItemsInInvoiceAdapter.ViewHolder>{

    Context context;
    List<OrderListResponse.ResultBean.ItemsBean> list;
    LayoutInflater inflater;
    public ItemsInInvoiceAdapter(Context context,List<OrderListResponse.ResultBean.ItemsBean> list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemsInInvoiceBinding binding= DataBindingUtil.inflate(inflater, R.layout.items_in_invoice,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemsInInvoiceBinding binding;
        public ViewHolder(ItemsInInvoiceBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){
            if(list.get(position).getType().equals("meal")){

                if(!list.get(position).getImage().equals("")){
                    Utilities.setCategoryImage(context,binding.imgItem,list.get(position).getImage());
                }else {
                    binding.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_meal_item));
                }
                binding.tvItemName.setText(list.get(position).getTitle());
            }else if(list.get(position).getType().equals("deal")){
                if(!list.get(position).getImage().equals("")){
                    Utilities.setCategoryImage(context,binding.imgItem,list.get(position).getImage());
                }else {
                    binding.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.deals_icon));
                }
                binding.tvItemName.setText(list.get(position).getTitle());

            }else{
                Utilities.setCategoryImage(context,binding.imgItem,list.get(position).getImage());
                binding.tvItemName.setText(list.get(position).getName());
            }
            binding.tvPrice.setText("SR "+list.get(position).getPriceForItem());
        }
    }
}
