package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemCategoryIconBinding;
import com.icashier.app.databinding.ItemMenuBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.MenuListener;
import com.icashier.app.model.MenuResponse;

import java.util.List;

public class MenuCategoryAdapter extends RecyclerView.Adapter<MenuCategoryAdapter.ViewHolder>{

    Context context;
    List<MenuResponse.MerchantBean.CategoriesBean> list;
    LayoutInflater inflater;
    MenuListener listener;

    public MenuCategoryAdapter(Context context,List<MenuResponse.MerchantBean.CategoriesBean> list,MenuListener listener){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryIconBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_category_icon,parent,false);
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

        ItemCategoryIconBinding binding;
        public ViewHolder(ItemCategoryIconBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position)
        {
            if(list.get(position).getId()==-1){

                    binding.imgCategory.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_meal_category));

            }else if(list.get(position).getId()==-2){
                binding.imgCategory.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_deal));
            }else if(list.get(position).getId()==-3){
                binding.imgCategory.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_all_items));
            }
            else{
                Utilities.setCategoryImage(context,binding.imgCategory,list.get(position).getImage());
            }

            if(list.get(position).isSelected()){
                binding.clParentLayout.setSelected(true);
                binding.imgCategory.setColorFilter(context.getResources().getColor(R.color.colorWhite));
            }else{
                binding.clParentLayout.setSelected(false);
                binding.imgCategory.setColorFilter(context.getResources().getColor(R.color.colorBlack));
            }

            binding.clParentLayout.setOnClickListener(V->{
                for(int i=0;i<list.size();i++){
                    list.get(i).setSelected(false);
                }
                if(list.get(position).isSelected()){
                    list.get(position).setSelected(false);
                }else{
                    list.get(position).setSelected(true);
                }
                listener.onCategoryClick(position);
                notifyDataSetChanged();
            });
        }
    }
}
