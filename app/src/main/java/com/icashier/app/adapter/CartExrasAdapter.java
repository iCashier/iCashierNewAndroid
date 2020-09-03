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
import com.icashier.app.databinding.ItemMenuExtrasBinding;
import com.icashier.app.model.CartItemModel;
import com.icashier.app.model.MenuResponse;

import java.util.List;

public class CartExrasAdapter extends RecyclerView.Adapter<CartExrasAdapter.ViewHolder>{

    Context context;
    List<CartItemModel.ExtrasBean> list;
    LayoutInflater inflater;
    public CartExrasAdapter(   Context context, List<CartItemModel.ExtrasBean> list){
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMenuExtrasBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_menu_extras,parent,false);
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

        ItemMenuExtrasBinding binding;
        public ViewHolder(ItemMenuExtrasBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){
            if(list.get(position).isChecked()){
                binding.llParentLayout.setVisibility(View.VISIBLE);
                if(list.get(position).getType().equals(AppConstant.RADIO_BUTTON)){
                    binding.clCb.setVisibility(View.GONE);
                    binding.clRb.setVisibility(View.VISIBLE);

                    binding.rbExtra.setChecked(true);
                    binding.rbExtra.setText(list.get(position).getTitle());
                    binding.tvPriceRb.setText("SR "+list.get(position).getPrice());
                }else if(list.get(position).getType().equals(AppConstant.CHECK_BOX)){
                    binding.clCb.setVisibility(View.VISIBLE);
                    binding.clRb.setVisibility(View.GONE);


                    binding.cbName.setChecked(true);
                    binding.cbName.setText(list.get(position).getTitle());
                    binding.tvPriceCB.setText("SR "+list.get(position).getPrice());
                }
            }else{
                binding.llParentLayout.setVisibility(View.GONE);
            }

        }
    }
}
