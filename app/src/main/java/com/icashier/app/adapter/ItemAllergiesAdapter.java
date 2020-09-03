package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemAllergiesBinding;
import com.icashier.app.databinding.ItemItemAllergiesBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.AllergiesModel;

import java.util.List;

public class ItemAllergiesAdapter extends RecyclerView.Adapter<ItemAllergiesAdapter.ViewHolder>{

    List<AllergiesModel.Result> list;
    Context context;

    public ItemAllergiesAdapter(Context context,List<AllergiesModel.Result> list){
        this.list=list;
        this.context=context;
    }
    @NonNull
    @Override
    public ItemAllergiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ItemItemAllergiesBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_item_allergies,viewGroup,false);
        return new ItemAllergiesAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAllergiesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bindData(i);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemItemAllergiesBinding binding;
        public ViewHolder(ItemItemAllergiesBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){

            Utilities.setCategoryImage(context,binding.img,list.get(position).getIcon());
            binding.img.setColorFilter(context.getResources().getColor(R.color.allergyColorRed));

        }
    }
}

