package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemInvoicesBinding;
import com.icashier.app.listener.InvoiceItemListener;
import com.icashier.app.helper.DateUtils;
import com.icashier.app.model.InvoiceListResponse;

import java.util.List;

public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.ViewHolder>{

    Context context;
    LayoutInflater inflater;
    List<InvoiceListResponse.ResultBean> list;
    InvoiceItemListener listener;
    public InvoicesAdapter(Context context,List<InvoiceListResponse.ResultBean> list,InvoiceItemListener listener){
        this.context=context;
        this.listener=listener;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInvoicesBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_invoices,parent,false);
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

        ItemInvoicesBinding binding;
        public ViewHolder(ItemInvoicesBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){

            binding.tvInvoiceId.setText(""+list.get(position).getInvoiceId());
            binding.tvCustomerName.setText(list.get(position).getCustomerName());
            binding.tvTitle.setText(list.get(position).getTitle());
            try {
                binding.tvDate.setText(DateUtils.convertUTCtoLocalTime2(list.get(position).getDate(),"dd MMM, yyyy"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            binding.imgDelete.setOnClickListener(V->{
                listener.onDeleteClick(position);
            });
        }
    }
}
