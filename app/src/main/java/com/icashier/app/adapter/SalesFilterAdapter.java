package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemTimeFilterBinding;
import com.icashier.app.listener.SalesFilterListener;
import com.icashier.app.model.WeekDaysModel;

import java.util.List;

public class SalesFilterAdapter extends RecyclerView.Adapter<SalesFilterAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    List<WeekDaysModel> list;
    SalesFilterListener listener;

    public SalesFilterAdapter(Context context,List<WeekDaysModel> list,SalesFilterListener listener){
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.list=list;
        this.listener=listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTimeFilterBinding binding=DataBindingUtil.inflate(inflater, R.layout.item_time_filter,parent,false);
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

        ItemTimeFilterBinding binding;

        public ViewHolder(ItemTimeFilterBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        void bindData(int position){
            binding.tv.setText(list.get(position).getDay());

            if(list.get(position).isSelected()){
                binding.cb.setChecked(true);
            }else{
                binding.cb.setChecked(false);
            }

            ShapeDrawable footerBackground = new ShapeDrawable();
            footerBackground.setShape(new OvalShape());

            if(position==0){
                int color = context.getResources().getColor(R.color.todayColor);
                footerBackground.getPaint().setColor(color);
                binding.view.setBackground(footerBackground);
            }else if(position==1){
                int color = context.getResources().getColor(R.color.weekColor);
                footerBackground.getPaint().setColor(color);
                binding.view.setBackground(footerBackground);
            }else if(position==2){
                int color = context.getResources().getColor(R.color.monthColor);
                footerBackground.getPaint().setColor(color);
                binding.view.setBackground(footerBackground);
            }else if(position==3){
                int color = context.getResources().getColor(R.color.monthColor2);
                footerBackground.getPaint().setColor(color);
                binding.view.setBackground(footerBackground);
            }

            binding.clParentLayout.setOnClickListener(V->{


                for(int i=0;i<list.size();i++){
                    if(i!=position){
                        list.get(i).setSelected(false);
                    }
                }
                if(!list.get(position).isSelected()){
                    list.get(position).setSelected(true);
                    listener.onFiltered();
                }

                notifyDataSetChanged();

            });

        }
    }
}
