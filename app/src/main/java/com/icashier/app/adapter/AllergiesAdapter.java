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
import com.icashier.app.databinding.ItemAllergiesBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.AllergiesModel;

import java.util.List;

public class AllergiesAdapter extends RecyclerView.Adapter<AllergiesAdapter.ViewHolder>{

    List<AllergiesModel.Result> list;
    Context context;

    public AllergiesAdapter(Context context,List<AllergiesModel.Result> list){
        this.list=list;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ItemAllergiesBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_allergies,viewGroup,false);
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

        ItemAllergiesBinding binding;
        public ViewHolder(ItemAllergiesBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){

            binding.tvName.setText(Utilities.isArabic()?list.get(position).getarabic_name():list.get(position).getName());
            Utilities.setCategoryImage(context,binding.img,list.get(position).getIcon());


            if(list.get(position).isSelected()){
                binding.img.setColorFilter(context.getResources().getColor(R.color.allergyColorRed));
            }else{
                binding.img.setColorFilter(context.getResources().getColor(R.color.allergyColor));
            }

            binding.img.setOnClickListener(V->{
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
