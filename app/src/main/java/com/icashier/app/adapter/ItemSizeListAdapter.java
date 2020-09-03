package com.icashier.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemSizeBinding;
import com.icashier.app.listener.ItemSizeListener;
import com.icashier.app.model.ItemPriceModel;

import java.util.ArrayList;
import java.util.List;

public class ItemSizeListAdapter extends BaseAdapter {
    private Context context;
    LayoutInflater inflater;
    List<String> itemSizeList;
    ItemSizeListener listener;
    List<ItemPriceModel> sizeData;
    List<String> addedSize;
    List<String> remainingSize= new ArrayList<>();
    List<ItemPriceModel> addedData=new ArrayList<>();
    View clView;



    public ItemSizeListAdapter(Context context,  List<String> itemSizeList,List<String> addedSize,List<ItemPriceModel> sizeData,View view,ItemSizeListener listener)
    {
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.itemSizeList=itemSizeList;
        this.sizeData=sizeData;
        this.listener=listener;
        this.clView=view;
        this.addedSize=addedSize;
    }
    private class ViewHolder {

        ItemSizeBinding binding;

    }


    @Override
    public int getCount() {
        return itemSizeList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemSizeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemSizeList.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater Inflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = Inflater.inflate(R.layout.item_country_picker, null);
            holder = new ViewHolder();
            holder.binding= DataBindingUtil.inflate(Inflater,R.layout.item_size,parent,false);
            holder.binding.getRoot().setTag(holder);
            bindData(position,holder.binding);
        return holder.binding.getRoot();
    }

    void bindData(int position,ItemSizeBinding binding) {
        binding.tvSize.setOnClickListener(V->{showPopup(binding );});


        binding.etPrice.setText(sizeData.get(position).getPrice());
        binding.tvSize.setText(sizeData.get(position).getSize());

        if(!binding.tvSize.getText().toString().equals(""))
        {
            binding.etPrice.setEnabled(true);
        }else
        {
            binding.etPrice.setEnabled(false);
        }
        if(sizeData.size()>1){
            if(position==sizeData.size()-1){
                binding.imgAdd.setVisibility(View.VISIBLE);
                binding.imgDelete.setVisibility(View.GONE);

                binding.imgAdd.setOnClickListener(V->{
                    //listener.onAddClick(position);
                    sizeData.add(sizeData.size()-1,new ItemPriceModel(binding.tvSize.getText().toString(),binding.etPrice.getText().toString()));
                });
            }else{
                binding.imgAdd.setVisibility(View.GONE);
                binding.imgDelete.setVisibility(View.VISIBLE);
                binding.imgDelete.setOnClickListener(V->{
                    addedSize.remove(binding.tvSize.getText().toString());
                    listener.onDeleteClick(position);
                });
            }
        }else{
            binding.imgAdd.setVisibility(View.VISIBLE);
            binding.imgDelete.setVisibility(View.GONE);
            binding.imgAdd.setOnClickListener(V->{
                //listener.onAddClick(position);
                sizeData.add(0,new ItemPriceModel(binding.tvSize.getText().toString(),binding.etPrice.getText().toString()));

            });
        }
    }

    //======================Mehtod to show primary category list popup=======================//
    private void showPopup(ItemSizeBinding binding) {
        remainingSize.clear();
        remainingSize.addAll(itemSizeList);
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
}
