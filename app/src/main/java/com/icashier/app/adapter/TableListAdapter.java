package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemExtrasBinding;
import com.icashier.app.model.TableListResponse;

import java.util.List;

public class TableListAdapter extends RecyclerView.Adapter<TableListAdapter.ViewHolder>{

    Context context;
    List<TableListResponse.ResultBean> tableList;
    LayoutInflater inflater;
    TableItemListener listener;
    public  TableListAdapter(Context context,List<TableListResponse.ResultBean> tableList,TableItemListener listener)
    {
        this.context=context;
        this.tableList=tableList;
        this.inflater=LayoutInflater.from(context);
        this.listener=listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExtrasBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_extras,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ItemExtrasBinding binding;
        public ViewHolder(ItemExtrasBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
         void bindData(int position)
         {
             binding.tvTittle.setText(tableList.get(position).getName());
             binding.imgDelete.setOnClickListener(V->{
                 listener.onDeleteClick(position);
             });

             binding.imgEdit.setOnClickListener(V->{
                 listener.onEditClick(position);
             });
         }
    }
}
