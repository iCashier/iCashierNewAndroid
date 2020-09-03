package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemOutletBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.SigninResponse;

import java.util.List;

public class OutletListAdapter extends RecyclerView.Adapter<OutletListAdapter.ViewHolder> {

    Context context;
    List<SigninResponse.ResultBean> list;
    LayoutInflater inflater;

    public OutletListAdapter(Context context,List<SigninResponse.ResultBean> list){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OutletListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemOutletBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_outlet,viewGroup,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OutletListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bindData(i);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemOutletBinding binding;
        public ViewHolder( ItemOutletBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

        }

         void  bindData(int position){
             Utilities.setCategoryImage(context,binding.imgLogo,list.get(position).getLogo());
             binding.rbTitle.setText(list.get(position).getTitle());
             binding.tvAddress.setText(list.get(position).getLocation());

             if(list.get(position).isSelected()){
                 binding.rbTitle.setChecked(true);
             }else{
                 binding.rbTitle.setChecked(false);
             }

             binding.llView.setOnClickListener(V->{
                 if(!list.get(position).isSelected()){
                     for(int i=0;i<list.size();i++){
                         if(i!=position) {
                             list.get(i).setSelected(false);
                         }
                     }
                     list.get(position).setSelected(true);

                 }
                 notifyDataSetChanged();
             });

        }
    }
}
