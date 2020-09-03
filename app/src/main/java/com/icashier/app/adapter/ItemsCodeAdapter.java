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
import com.icashier.app.databinding.ItemAllItemsBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CodeListItemListener;
import com.icashier.app.model.AllCodesListResponse;

import java.util.List;

public class ItemsCodeAdapter extends RecyclerView.Adapter<ItemsCodeAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    List<AllCodesListResponse.ResultBean> list;
    CodeListItemListener listener;

    public ItemsCodeAdapter(Context context,List<AllCodesListResponse.ResultBean> allCodesList,CodeListItemListener listener){
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.list=allCodesList;
        this.listener=listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAllItemsBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_all_items,parent,false);
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

        ItemAllItemsBinding binding;
        public ViewHolder(ItemAllItemsBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        public void bindData(int position){

            setData(position);
            binding.clParentLayout.setOnClickListener(V->{

                for(int i=0;i<list.size();i++){
                    list.get(i).setSelected(false);
                }
                if(list.get(position).isSelected()){
                    list.get(position).setSelected(false);
                }else{
                    list.get(position).setSelected(true);
                    listener.onSelected(position);
                }

                notifyDataSetChanged();

            });

            binding.imgDelete.setOnClickListener(V->{
                listener.onDelete(position);
                for(int i=0;i<list.size();i++){
                    list.get(i).setSelected(false);
                }
                if(list.get(position).isSelected()){
                    list.get(position).setSelected(false);
                }else{
                    list.get(position).setSelected(true);
                    listener.onSelected(position);
                }

                notifyDataSetChanged();
            });

            binding.imgEdit.setOnClickListener(V->{
                listener.onEdit(position);
                for(int i=0;i<list.size();i++){
                    list.get(i).setSelected(false);
                }
                if(list.get(position).isSelected()){
                    list.get(position).setSelected(false);
                }else{
                    list.get(position).setSelected(true);
                    listener.onSelected(position);
                }

                notifyDataSetChanged();
            });
        }

        //==================Mehtod to set data============//
        void setData(int position)
        {
            if(list.get(position).getType().equals(AppConstant.TYPE_DINE_IN)){
                binding.imgType.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_dine_in));
                binding.tvOrderNo.setText(list.get(position).getTable_id());
            }else if(list.get(position).getType().equals(AppConstant.TYPE_PICKUP)){
                binding.imgType.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_takeout));
                binding.tvOrderNo.setText(list.get(position).getName());

            }else if(list.get(position).getType().equals(AppConstant.TYPE_ITEM)){
                binding.imgType.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_burger_black));
                binding.tvOrderNo.setText(list.get(position).getName());

            }else if(list.get(position).getType().equals(AppConstant.TYPE_MEALS)){
                binding.imgType.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_meals_black));
                binding.tvOrderNo.setText(list.get(position).getName());

            }else if(list.get(position).getType().equals(AppConstant.TYPE_DEALS)){
                binding.imgType.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_title));
                binding.tvOrderNo.setText(list.get(position).getName());

            }else if(list.get(position).getType().equals(AppConstant.TYPE_COUPON)){
                binding.imgType.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_coupons_black));
                binding.tvOrderNo.setText(list.get(position).getName());

            }

            if(list.get(position).getPayment()!=null) {
                if (list.get(position).getPayment().contains(AppConstant.CASH)) {
                    binding.imgCash.setVisibility(View.VISIBLE);
                }else{
                    binding.imgCash.setVisibility(View.GONE);

                }
                if (list.get(position).getPayment().contains(AppConstant.CREDIT_CARD)) {
                    binding.imgCreditCard.setVisibility(View.VISIBLE);
                }else{
                    binding.imgCreditCard.setVisibility(View.GONE);
                }
                if (list.get(position).getPayment().contains(AppConstant.PAYPAL)) {
                    binding.imgPaypal.setVisibility(View.VISIBLE);
                }else{
                    binding.imgPaypal.setVisibility(View.GONE);
                }
            }

            if(list.get(position).getDelivery()!=null) {
                if (list.get(position).getDelivery().contains(AppConstant.TYPE_PICKUP)) {
                    binding.imgPickup.setVisibility(View.VISIBLE);
                }  else {
                    binding.imgPickup.setVisibility(View.GONE);
                }
                if (list.get(position).getDelivery().contains(AppConstant.TYPE_DELIVERY)) {
                    binding.imgDelivey.setVisibility(View.VISIBLE);
                } else {
                    binding.imgDelivey.setVisibility(View.GONE);
                }
                if (list.get(position).getDelivery().contains(AppConstant.TYPE_INSTA_DELIVERY)) {
                    binding.imgInstaDelivery.setVisibility(View.VISIBLE);
                } else {
                    binding.imgInstaDelivery.setVisibility(View.GONE);
                }
                if (list.get(position).getDelivery().contains(AppConstant.TYPE_SWIFT_DELIVERY)) {
                    binding.imgSwiftDelivery.setVisibility(View.VISIBLE);
                } else {
                    binding.imgSwiftDelivery.setVisibility(View.GONE);
                }
            } else {
                binding.imgDelivey.setVisibility(View.GONE);
                binding.imgPickup.setVisibility(View.GONE);
                binding.imgSwiftDelivery.setVisibility(View.GONE);
                binding.imgInstaDelivery.setVisibility(View.GONE);

                if(list.get(position).getType().contains(AppConstant.TYPE_DINE_IN)) {
                    binding.imgDelivey.setVisibility(View.VISIBLE);
                    binding.imgDelivey.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_dine_in));
                }else if(list.get(position).getType().contains(AppConstant.TYPE_PICKUP)) {
                    binding.imgDelivey.setVisibility(View.VISIBLE);
                    binding.imgDelivey.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_takeout));
                }
            }


            if(list.get(position).isSelected()){
                binding.clParentLayout.setSelected(true);
            }else{
                binding.clParentLayout.setSelected(false);
            }


        }
    }
}
