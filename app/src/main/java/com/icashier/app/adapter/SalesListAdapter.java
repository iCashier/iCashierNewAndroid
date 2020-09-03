package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemSalesBinding;
import com.icashier.app.helper.DateUtils;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.OrderListResponse;

import java.util.List;

public class SalesListAdapter extends RecyclerView.Adapter<SalesListAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    List<OrderListResponse.ResultBean> list;

    public SalesListAdapter(Context context,List<OrderListResponse.ResultBean> orderList){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.list=orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSalesBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_sales,parent,false);
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

        ItemSalesBinding binding;
        public ViewHolder(ItemSalesBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){
            binding.tvOrderNo.setText("#"+list.get(position).getSequence_no());
            binding.tvPrice.setText("SR "+ Utilities.formatPrice(Float.parseFloat(list.get(position).getTotal())));

            try {
                binding.tvTime.setText(DateUtils.convertUTCtoLocalTime2(list.get(position).getDate(),"hh:mm"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
