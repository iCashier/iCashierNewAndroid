package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.ItemCategoryImageBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.SelectIconListener;
import com.icashier.app.model.CategoryImagesResponse;

import java.util.List;

public class CategoryIconsAdapter extends RecyclerView.Adapter<CategoryIconsAdapter.ViewHolder>{

    Context context;
    LayoutInflater inflater;
    SelectIconListener listener;
    List<CategoryImagesResponse.ResultBean> imagesList;
    public CategoryIconsAdapter(Context context,  List<CategoryImagesResponse.ResultBean> imagesList,SelectIconListener listener){
        this.context=context;
        this.imagesList=imagesList;
        this.listener=listener;
        inflater=LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryImageBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_category_image,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemCategoryImageBinding binding;
        public ViewHolder(ItemCategoryImageBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){
            Utilities.setCategoryImage(context,binding.imgCategory, ServerConstants.IMAGE_BASE_URL+imagesList.get(position).getImage());

            if(imagesList.get(position).isSelected()){
                binding.clParentLayout.setSelected(true);
            }else
            {
                binding.clParentLayout.setSelected(false);
            }

            binding.imgCategory.setOnClickListener(V->{
                for(int i=0;i<imagesList.size();i++){
                    imagesList.get(i).setSelected(false);
                }
                imagesList.get(position).setSelected(true);
                listener.onIconSelected(position);
                notifyDataSetChanged();
            });
        }
    }
}
