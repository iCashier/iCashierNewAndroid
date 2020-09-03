package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.databinding.ItemCheckoutExtrasBinding;
import com.icashier.app.databinding.ItemMenuExtrasBinding;
import com.icashier.app.model.CartItemModel;
import com.icashier.app.model.MenuResponse;

import java.util.List;

public class CheckoutItemExtrasAdapter extends RecyclerView.Adapter<CheckoutItemExtrasAdapter.ViewHolder>{

    Context context;
    List<CartItemModel.ExtrasBean> list;
    LayoutInflater inflater;
    public CheckoutItemExtrasAdapter(   Context context, List<CartItemModel.ExtrasBean> list){
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCheckoutExtrasBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_checkout_extras,parent,false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemCheckoutExtrasBinding binding;
        public ViewHolder(ItemCheckoutExtrasBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){
            if(list.get(position).isChecked()){
                binding.llParentLayout.setVisibility(View.VISIBLE);
                binding.tvExtraName.setText(list.get(position).getTitle());
                binding.tvPrice.setText(list.get(position).getPrice());
            }else{
                binding.llParentLayout.setVisibility(View.GONE);
            }

        }
    }
}
