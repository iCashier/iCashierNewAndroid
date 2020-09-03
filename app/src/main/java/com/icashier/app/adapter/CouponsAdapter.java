package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemCashierBinding;
import com.icashier.app.databinding.ItemCouponBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.ExistingItemListener;
import com.icashier.app.model.GetCashiersResponse;
import com.icashier.app.model.GetCouponsResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.internal.Util;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder> {

    Context context;
    List<GetCouponsResponse.ResultBean> couponsList;
    LayoutInflater inflater;
    ExistingItemListener listener;
    boolean showCb;

    public CouponsAdapter(Context context, List<GetCouponsResponse.ResultBean> couponsList, ExistingItemListener listener,boolean showCb) {
        this.couponsList = couponsList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.showCb=showCb;
    }

    @NonNull
    @Override
    public CouponsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCouponBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_coupon, parent, false);
        return new CouponsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponsAdapter.ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return couponsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemCouponBinding binding;

        public ViewHolder(ItemCouponBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(int position) {
            if(showCb){
                binding.cbCoupon.setVisibility(View.VISIBLE);
                binding.imgDelete.setVisibility(View.GONE);
                binding.imgEdit.setVisibility(View.GONE);
            }else{
                binding.cbCoupon.setVisibility(View.GONE);
                binding.imgDelete.setVisibility(View.VISIBLE);
                binding.imgEdit.setVisibility(View.VISIBLE);
            }

            if(couponsList.get(position).isSelected()){
                binding.cbCoupon.setChecked(true);
            }else{
                binding.cbCoupon.setChecked(false);
            }
            binding.tvTitle.setText(couponsList.get(position).getTitle());
            if (couponsList.get(position).getType().equals("$"))
                binding.tvDiscount.setText("SR " + couponsList.get(position).getValue() + " Off");
            else binding.tvDiscount.setText(couponsList.get(position).getValue() + "% Off");
            binding.tvDate.setText(Utilities.formatDate(couponsList.get(position).getExpiry_date(), "yyyy-MM-dd hh:mm:ss", "MMM dd, yyyy"));
            binding.imgDelete.setOnClickListener(V -> {
                listener.onDeleteClick(position);
            });
            binding.imgEdit.setOnClickListener(V -> {
                listener.onEditClick(position);
            });

            binding.cbCoupon.setOnClickListener(V->{
                if(couponsList.get(position).isSelected()){
                    couponsList.get(position).setSelected(false);
                }else{
                    for(int i=0;i<couponsList.size();i++){
                        couponsList.get(i).setSelected(false);
                    }
                    couponsList.get(position).setSelected(true);

                }
                notifyDataSetChanged();
            });
        }
    }
}
