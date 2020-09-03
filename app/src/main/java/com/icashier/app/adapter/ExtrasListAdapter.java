package com.icashier.app.adapter;

import android.content.ClipData;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemExtrasBinding;
import com.icashier.app.listener.ExtraItemListener;
import com.icashier.app.model.ExtrasListResponse;

import java.util.List;

public class ExtrasListAdapter extends RecyclerView.Adapter<ExtrasListAdapter.ViewHolder>{

    Context context;
    List<ExtrasListResponse.ResultBean> extrasList;
    LayoutInflater inflater;
    ExtraItemListener listener;
    public ExtrasListAdapter(Context context, List<ExtrasListResponse.ResultBean> extrasList,ExtraItemListener listener )
    {
        this.context=context;
        this.extrasList=extrasList;
        inflater=LayoutInflater.from(context);
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
        return extrasList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemExtrasBinding binding;
        public ViewHolder(ItemExtrasBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position)
        {
            binding.tvTittle.setText(extrasList.get(position).getTitle());
            binding.tvPrice.setText("SR "+extrasList.get(position).getPrice());
            binding.imgDelete.setOnClickListener(V->{
                listener.onDeleteClick(position);
            });

            binding.imgEdit.setOnClickListener(V->{
                listener.onEditClick(position);

            });

        }
    }
}
