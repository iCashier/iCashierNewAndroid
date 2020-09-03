package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.databinding.ItemSizeBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.DecimalDigitsInputFilter;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.ItemSizeListener;
import com.icashier.app.model.ItemPriceModel;
import com.icashier.app.model.ItemSizesResponse;

import java.util.ArrayList;
import java.util.List;

public class ItemSizeAdapter extends RecyclerView.Adapter<ItemSizeAdapter.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    private List<String> itemSizeList;
    ItemSizeListener listener;
    private List<ItemPriceModel> sizeData;
    private List<String> addedSize;
    private List<String> remainingSize= new ArrayList<>();
    List<ItemPriceModel> addedData=new ArrayList<>();
    private View clView;

    public ItemSizeAdapter(Context context, List<String> itemSizeList,List<String> addedSize,List<ItemPriceModel> sizeData,View view,ItemSizeListener listener)
    {
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.itemSizeList=itemSizeList;
        this.sizeData=sizeData;
        this.listener=listener;
        this.clView=view;
        this.addedSize=addedSize;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSizeBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_size,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return sizeData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemSizeBinding binding;
        public ViewHolder(ItemSizeBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        void bindData(int position)
        {
            binding.etPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});

            binding.tvSize.setOnClickListener(V->{showPopup();});

            binding.etPrice.setText(sizeData.get(position).getPrice());
            if(sizeData.get(position).getSize().equalsIgnoreCase(AppConstant.DEFAULT_SIZE)&&Utilities.isArabic()) {
                binding.tvSize.setText(context.getText(R.string.default_size));
            }else{
                binding.tvSize.setText(sizeData.get(position).getSize());

            }

            if(!binding.tvSize.getText().toString().equals(""))
            {
                binding.etPrice.setEnabled(true);
            }else
            {
                binding.etPrice.setEnabled(false);
            }
            if(sizeData.size()>1){
                if(position==sizeData.size()-1){
                    //binding.imgAdd.setVisibility(View.VISIBLE);
                    //binding.imgDelete.setVisibility(View.GONE);

                    binding.imgAdd.setOnClickListener(V->{
                        if(position<itemSizeList.size()-1){
                            addSize();
                            if(remainingSize.size()>0) {
                                listener.onAddClick(position, remainingSize.get(0));
                            }


                        }
                            //sizeData.add(sizeData.size()-1,new ItemPriceModel(binding.tvSize.getText().toString(),binding.etPrice.getText().toString()));
                    });
                }else{
                    //binding.imgAdd.setVisibility(View.GONE);
                   // binding.imgDelete.setVisibility(View.VISIBLE);
                    binding.imgDelete.setOnClickListener(V->{
                        addedSize.remove(binding.tvSize.getText().toString());
                        listener.onDeleteClick(position);
                    });
                }
            }else{
               // binding.imgAdd.setVisibility(View.VISIBLE);
                //binding.imgDelete.setVisibility(View.GONE);
                binding.imgAdd.setOnClickListener(V->{
                    if(position<itemSizeList.size()-1){
                        addSize();
                        if(remainingSize.size()>0) {
                            listener.onAddClick(position, remainingSize.get(0));
                        }
                    }
                    //sizeData.add(0,new ItemPriceModel(binding.tvSize.getText().toString(),binding.etPrice.getText().toString()));
                });
            }
            if(position==0){
                binding.imgAdd.setVisibility(View.VISIBLE);
                binding.imgDelete.setVisibility(View.GONE);

                binding.imgAdd.setOnClickListener(V->{
                    if(position<itemSizeList.size()-1&&itemSizeList.size()!=addedSize.size()){
                        addSize();
                        if(remainingSize.size()>0) {
                            listener.onAddClick(position, remainingSize.get(0));
                        }
                    }
                    //sizeData.add(0,new ItemPriceModel(binding.tvSize.getText().toString(),binding.etPrice.getText().toString()));

                });

            }else{
                binding.imgAdd.setVisibility(View.GONE);
                binding.imgDelete.setVisibility(View.VISIBLE);
                binding.imgDelete.setOnClickListener(V->{
                    addedSize.remove(binding.tvSize.getText().toString());
                    listener.onDeleteClick(position);
                });

            }
        }



        //======================Mehtod to show primary category list popup=======================//
        private void showPopup() {
            remainingSize.clear();

            for (String itemSize: itemSizeList) {
                if(itemSize.equals(AppConstant.DEFAULT_SIZE)&&Utilities.isArabic()) {
                    remainingSize.add(context.getString(R.string.default_size));
                }else {
                    remainingSize.add(itemSize);
                }
            }
            
            for(int i=0;i<addedSize.size();i++){
                if(remainingSize.contains(addedSize.get(i))){
                    remainingSize.remove(addedSize.get(i));
                }
            }

            ArrayAdapter listPopupAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,remainingSize);

            ListPopupWindow popupWindow=new ListPopupWindow(context);
            popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int index, long id) {

                    if(binding.tvSize.getText().toString().equals("")){
                        
                        binding.tvSize.setText(remainingSize.get(index));
                        addedSize.add(remainingSize.get(index));
                    }else{
                        addedSize.remove(binding.tvSize.getText().toString());
                        binding.tvSize.setText(remainingSize.get(index));
                        addedSize.add(remainingSize.get(index));
                    }
                    binding.etPrice.setEnabled(true);
                    popupWindow.dismiss();
                }
            });

            popupWindow.setContentWidth(binding.tvSize.getWidth());
            popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            popupWindow.setAnchorView(binding.tvSize);
            popupWindow.setAdapter(listPopupAdapter);
            popupWindow.show();

        }

        private void addSize(){
            remainingSize.clear();
            remainingSize.addAll(itemSizeList);
            for(int i=0;i<addedSize.size();i++){
                if(remainingSize.contains(addedSize.get(i))){
                    remainingSize.remove(addedSize.get(i));
                }
            }

            if(remainingSize.size()>0) {
                addedSize.add(remainingSize.get(0));
            }

            binding.etPrice.setEnabled(true);
            notifyDataSetChanged();
        }


    }
}
