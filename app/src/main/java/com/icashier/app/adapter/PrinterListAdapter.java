package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemPrinterBinding;
import com.icashier.app.model.PrinterListModel;

import java.util.List;

public class PrinterListAdapter extends RecyclerView.Adapter<PrinterListAdapter.ViewHolder> {

    Context context;
    List<PrinterListModel> list;
    SelectPrinterListener listener;

    public PrinterListAdapter(Context context, List<PrinterListModel> list,SelectPrinterListener listener){
        this.context=context;
        this.list=list;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemPrinterBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.item_printer,viewGroup,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindData(i);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemPrinterBinding binding;
        public ViewHolder(ItemPrinterBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        void bindData(int position){
            binding.tvInfo.setText(list.get(position).getTarget());
            binding.tvName.setText(list.get(position).getName());

            binding.llParent.setOnClickListener(V->{
                listener.onClick(list.get(position).getName(),list.get(position).getTarget());
            });
        }


    }

    public interface SelectPrinterListener{
        void onClick(String name,String target);
    }
}