package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.ItemCashierBinding;
import com.icashier.app.databinding.ItemExistingItemBinding;
import com.icashier.app.helper.DateUtils;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.ExistingItemListener;
import com.icashier.app.model.ExistingItemList;
import com.icashier.app.model.GetCashiersResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CashiersAdapter extends RecyclerView.Adapter<CashiersAdapter.ViewHolder> {

    Context context;
    List<GetCashiersResponse.CashiersBean> cashierList;
    LayoutInflater inflater;
    ExistingItemListener listener;

    public CashiersAdapter(Context context, List<GetCashiersResponse.CashiersBean> cashierList, ExistingItemListener listener) {
        this.cashierList = cashierList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CashiersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCashierBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_cashier, parent, false);
        return new CashiersAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CashiersAdapter.ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return cashierList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemCashierBinding binding;

        public ViewHolder(ItemCashierBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(int position) {

            Utilities.setCategoryImage(context,binding.imgImage,cashierList.get(position).getImage());
            binding.tvName.setText(cashierList.get(position).getName());
            try {
                binding.tvDate.setText(context.getString(R.string.joined)+" "+ DateUtils.convertUTCtoLocalTime2(cashierList.get(position).getSignup(),"dd MMM, yyyy"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            binding.imgDelete.setOnClickListener(V -> {
                listener.onDeleteClick(position);
            });
            binding.imgEdit.setOnClickListener(V -> {
                listener.onEditClick(position);
            });
        }
    }
}
