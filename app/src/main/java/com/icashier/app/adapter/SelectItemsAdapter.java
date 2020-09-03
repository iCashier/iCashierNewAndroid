package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;

import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.ItemMealItemBinding;
import com.icashier.app.databinding.ItemTodaySpecialBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.ExistingItemList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SelectItemsAdapter extends RecyclerView.Adapter<SelectItemsAdapter.ViewHolder> {

    Context context;
    List<ExistingItemList.ResultBean> existingItemList;
    LayoutInflater inflater;
    List<String> priceArray=new ArrayList<>();
    List<String> sizeArray=new ArrayList<>();

    public SelectItemsAdapter(Context context,List<ExistingItemList.ResultBean> existingItemList){
        this.existingItemList=existingItemList;
        this.context=context;
        this.inflater=LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMealItemBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_meal_item,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return existingItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ItemMealItemBinding binding;
        public ViewHolder(ItemMealItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        void bindData(int position){
            binding.tvItemName.setText(existingItemList.get(position).getName());
            if(Integer.parseInt(existingItemList.get(position).getQty())<0) {
                binding.tvQuantity.setText(R.string.unlimited);

            }else{
                binding.tvQuantity.setText(existingItemList.get(position).getQty());
            }
            Utilities.setCategoryImage(context,binding.imgItem, ServerConstants.IMAGE_BASE_URL+existingItemList.get(position).getImage());


            sizeArray.clear();
            priceArray.clear();
            sizeArray.addAll(Arrays.asList(existingItemList.get(position).getSize().split(",")));
            priceArray.addAll(Arrays.asList(existingItemList.get(position).getPrice().split(",")));

            if(existingItemList.get(position).getSelectedSize()==null) {
                if(sizeArray.size()>0){
                    if(sizeArray.get(0).equalsIgnoreCase(AppConstant.DEFAULT_SIZE)){
                        binding.tvItemSize.setText(context.getString(R.string.default_size));

                    }else{
                        binding.tvItemSize.setText(sizeArray.get(0));
                    }
                    binding.tvItemPrice.setText(priceArray.get(0));
                    existingItemList.get(position).setSelectedPrice(priceArray.get(0));
                    existingItemList.get(position).setSelectedSize(sizeArray.get(0));
                }else{
                    if(existingItemList.get(position).getSize().equalsIgnoreCase(AppConstant.DEFAULT_SIZE)){
                        binding.tvItemSize.setText(context.getString(R.string.default_size));
                    }else{
                        binding.tvItemSize.setText(existingItemList.get(position).getSize());
                    }

                    binding.tvItemPrice.setText(existingItemList.get(position).getPrice());
                    existingItemList.get(position).setSelectedPrice(existingItemList.get(position).getPrice());
                    existingItemList.get(position).setSelectedSize(existingItemList.get(position).getSize());
                }

            }else{
                if(existingItemList.get(position).getSelectedSize().equalsIgnoreCase(AppConstant.DEFAULT_SIZE)){
                    binding.tvItemSize.setText(context.getString(R.string.default_size));
                }else{
                    binding.tvItemSize.setText(existingItemList.get(position).getSelectedSize());

                }
                binding.tvItemPrice.setText(existingItemList.get(position).getSelectedPrice());
            }


            if(existingItemList.get(position).isSelected()){
                binding.cbItem.setChecked(true);
            }else{
                binding.cbItem.setChecked(false);
            }

            binding.cbItem.setOnClickListener(V->{
                if(existingItemList.get(position).isSelected()){
                    existingItemList.get(position).setSelected(false);
                }else{
                    binding.cbItem.setChecked(false);
                    existingItemList.get(position).setSelected(true);
                }
                notifyDataSetChanged();
            });

            binding.tvItemSize.setOnClickListener(V->{
                sizeArray.clear();
                priceArray.clear();
                sizeArray.addAll(Arrays.asList(existingItemList.get(position).getSize().split(",")));
                priceArray.addAll(Arrays.asList(existingItemList.get(position).getPrice().split(",")));
                showSizeListPopup(position);
            });
        }

        //======================Mehtod to show size list popup=======================//
        private void showSizeListPopup(int position) {
            if(sizeArray.contains(AppConstant.DEFAULT_SIZE)) {
                sizeArray.set(sizeArray.indexOf(AppConstant.DEFAULT_SIZE), context.getString(R.string.default_size));
            }
            ArrayAdapter listPopupAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,sizeArray);

            ListPopupWindow popupWindow=new ListPopupWindow(context);
            popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int index, long id) {

                    binding.tvItemSize.setText(sizeArray.get(index));
                    existingItemList.get(position).setSelectedPrice(priceArray.get(index));
                    if(sizeArray.get(index).equals(R.string.default_size)){
                        existingItemList.get(position).setSelectedSize(AppConstant.DEFAULT_SIZE);

                    }else{
                        existingItemList.get(position).setSelectedSize(sizeArray.get(index));
                    }
                    binding.tvItemPrice.setText(priceArray.get(index));
                    popupWindow.dismiss();
                }
            });

            popupWindow.setContentWidth(binding.llSize.getWidth());
            popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            popupWindow.setAnchorView(binding.tvItemSize);
            popupWindow.setAdapter(listPopupAdapter);
            popupWindow.show();


        }


    }


}
