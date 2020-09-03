package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemTopItemsBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.SalesDataModel;

import java.util.List;

import okhttp3.internal.Util;

public class TopItemsAdapter extends RecyclerView.Adapter<TopItemsAdapter.ViewHolder> {

    Context context;
    List<SalesDataModel.ResultBeanX.TopItemsBean> list;
    LayoutInflater inflater;
    public TopItemsAdapter(Context context, List<SalesDataModel.ResultBeanX.TopItemsBean> list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTopItemsBinding binding=DataBindingUtil.inflate(inflater,R.layout.item_top_items,parent,false);
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

        ItemTopItemsBinding binding;
        public ViewHolder(ItemTopItemsBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){
            binding.tvItemName.setText(list.get(position).getName());
            binding.tvCount.setText(""+list.get(position).getSale_count());

            Utilities.setCategoryImage(context,binding.imgItem,list.get(position).getImage());
        }
    }
}
