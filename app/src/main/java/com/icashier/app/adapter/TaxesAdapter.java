package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemCouponBinding;
import com.icashier.app.databinding.ItemTaxBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.ExistingItemListener;
import com.icashier.app.model.GetCouponsResponse;
import com.icashier.app.model.GetTaxListResponse;

import java.util.List;

public class TaxesAdapter extends RecyclerView.Adapter<TaxesAdapter.ViewHolder> {

    Context context;
    List<GetTaxListResponse.ResultBean> taxList;
    LayoutInflater inflater;
    ExistingItemListener listener;
    boolean show;

    public TaxesAdapter(Context context, List<GetTaxListResponse.ResultBean> taxList, ExistingItemListener listener,boolean show) {
        this.taxList = taxList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.show=show;
    }

    @NonNull
    @Override
    public TaxesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaxBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_tax, parent, false);
        return new TaxesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaxesAdapter.ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return taxList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemTaxBinding binding;

        public ViewHolder(ItemTaxBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(int position) {
            binding.tvTitle.setText(taxList.get(position).getTitle());
            if (taxList.get(position).getType().equals("$"))
                binding.tvDiscount.setText("SR " + taxList.get(position).getValue() );
            else binding.tvDiscount.setText(taxList.get(position).getValue() + "%");
//            binding.tvDate.setText(Utilities.formatDate(taaxList.get(position).getExpiry_date(), "yyyy-MM-dd hh:mm:ss", "MMM dd, yyyy"));
            binding.imgDelete.setOnClickListener(V -> {
                listener.onDeleteClick(position);
            });
            binding.imgEdit.setOnClickListener(V -> {
                listener.onEditClick(position);
            });

            if(show){
                binding.imgDelete.setVisibility(View.VISIBLE);
                binding.imgEdit.setVisibility(View.VISIBLE);
            }else{
                binding.imgDelete.setVisibility(View.GONE);
                binding.imgEdit.setVisibility(View.GONE);
            }
        }
    }
}
