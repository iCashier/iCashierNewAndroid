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
import com.icashier.app.model.OrderListResponse;

import java.util.List;

public class OrderItemExtrasAdapter extends RecyclerView.Adapter<OrderItemExtrasAdapter.ViewHolder>{

    Context context;
    List<OrderListResponse.ResultBean.ItemsBean.ExtrasAddedToCartBean> list;
    LayoutInflater inflater;
    public OrderItemExtrasAdapter(   Context context, List<OrderListResponse.ResultBean.ItemsBean.ExtrasAddedToCartBean> list){
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
                binding.llParentLayout.setVisibility(View.VISIBLE);
                binding.tvExtraName.setText(list.get(position).getTitle());
                binding.tvPrice.setText(list.get(position).getPrice());


        }
    }
}
