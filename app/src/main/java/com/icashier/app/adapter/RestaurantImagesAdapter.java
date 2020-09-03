package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.ItemImageBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.SelectImageListener;

import java.util.List;

public class RestaurantImagesAdapter extends RecyclerView.Adapter<RestaurantImagesAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    SelectImageListener listener;
    List<String> imagesList;
    public RestaurantImagesAdapter(Context context,List<String> imagesList,SelectImageListener listener){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.listener=listener;
        this.imagesList=imagesList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImageBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_image,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemImageBinding binding;
        public ViewHolder(ItemImageBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position)
        {
            if(position<imagesList.size()){
                if(imagesList.get(position).contains("http")||imagesList.contains("https"))
                {
                    Utilities.setCategoryImage(context,binding.imgItem, imagesList.get(position));
                    binding.imgCross.setVisibility(View.VISIBLE);
                    binding.imgPlus.setVisibility(View.GONE);

                }else
                {
                    binding.imgItem.setImageBitmap(BitmapFactory.decodeFile(imagesList.get(position)));
                    binding.imgCross.setVisibility(View.VISIBLE);
                    binding.imgPlus.setVisibility(View.GONE);
                }
            }else{
                binding.imgCross.setVisibility(View.GONE);
                binding.imgPlus.setVisibility(View.VISIBLE);
            }
            binding.imgPlus.setOnClickListener(V->{
                listener.onImageSelected(position);
            });

            binding.imgCross.setOnClickListener(V->{
                listener.onCrossClicked(position);
            });
        }
    }
}
