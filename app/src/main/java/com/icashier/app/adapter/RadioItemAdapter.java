package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.databinding.ItemRadioExtraBinding;
import com.icashier.app.model.ExtrasListResponse;

import java.util.ArrayList;
import java.util.List;

public class RadioItemAdapter extends RecyclerView.Adapter<RadioItemAdapter.ViewHolder>{

    Context context;
    List<ExtrasListResponse.ResultBean> extraList;
    LayoutInflater inflater;
    public RadioItemAdapter(Context context,List<ExtrasListResponse.ResultBean> extraList)
    {
        this.context=context;
        this.extraList=extraList;
        this.inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRadioExtraBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_radio_extra,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return extraList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemRadioExtraBinding binding;
        public ViewHolder(ItemRadioExtraBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        void bindData(int position)
        {
            binding.rbExtra.setText(extraList.get(position).getTitle());
            binding.tvPrice.setText("SR "+extraList.get(position).getPrice());

            if(extraList.get(position).isChecked()){
                binding.rbExtra.setChecked(true);
            }else{
                binding.rbExtra.setChecked(false);
            }
            binding.getRoot().setOnClickListener(V->{
                if(extraList.get(position).isChecked()){
                    extraList.get(position).setChecked(false);
                }else
                {
                    extraList.get(position).setChecked(true);
                }
                notifyDataSetChanged();
            });
        }

    }
}
