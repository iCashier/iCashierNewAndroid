package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemWeekDaysBinding;
import com.icashier.app.model.WeekDaysModel;

import java.util.ArrayList;
import java.util.List;

public class WeekDaysAdapter extends RecyclerView.Adapter<WeekDaysAdapter.ViewHolder>{

    Context context;
    LayoutInflater inflater;
    List<WeekDaysModel> list;
    public WeekDaysAdapter(Context context,List<WeekDaysModel> list){
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.list=list;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWeekDaysBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_week_days,parent,false);
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

        ItemWeekDaysBinding binding;
        public ViewHolder(ItemWeekDaysBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        void bindData(int position){
            binding.tvDay.setText(list.get(position).getDay());

            if(list.get(position).isSelected()){
                binding.tvDay.setSelected(true);
            }else{
                binding.tvDay.setSelected(false);
            }

            binding.tvDay.setOnClickListener(V->{
                if(list.get(position).isSelected()){
                    list.get(position).setSelected(false);
                }else{
                    list.get(position).setSelected(true);
                }
                notifyDataSetChanged();
            });
        }
    }


}
