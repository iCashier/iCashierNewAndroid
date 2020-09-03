package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemDealBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.ExtraItemListener;
import com.icashier.app.listener.PrimaryCategoryListener;
import com.icashier.app.model.ExistingDealsModel;
import com.icashier.app.model.ExtrasListResponse;

import java.util.List;

import okhttp3.internal.Util;

public class ExistingDealsAdapter extends RecyclerView.Adapter<ExistingDealsAdapter.ViewHolder> {

    Context context;
    List<ExistingDealsModel.ResultBean> list;
    LayoutInflater inflater;
    ExtraItemListener listener;
    boolean showCheckbox;
    boolean isMultiSelect;
    

    public ExistingDealsAdapter(Context context,List<ExistingDealsModel.ResultBean> list,ExtraItemListener listener,boolean showCheckbox,boolean isMultiSelect){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
        this.listener=listener;
        this.showCheckbox=showCheckbox;
        this.isMultiSelect=isMultiSelect;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDealBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_deal,parent,false);
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

        ItemDealBinding binding;
        public ViewHolder(ItemDealBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){
            binding.tvName.setText(list.get(position).getTitle());
            binding.tvDate.setText(Utilities.formatDate(list.get(position).getFromDate(), "dd-MM-yyyy", "dd MMM, yyyy")+" - "+
                    (Utilities.formatDate(list.get(position).getToDate(), "dd-MM-yyyy", "dd MMM, yyyy")));
            Utilities.setCategoryImage(context,binding.imgItem,list.get(position).getImage());


            if(showCheckbox){
                binding.cbDeal.setVisibility(View.VISIBLE);
                binding.imgEdit.setVisibility(View.GONE);
                binding.imgDelete.setVisibility(View.GONE);
            }else{
                binding.cbDeal.setVisibility(View.GONE);
                binding.imgEdit.setVisibility(View.VISIBLE);
                binding.imgDelete.setVisibility(View.VISIBLE);
                binding.imgDelete.setOnClickListener(V->{
                    listener.onDeleteClick(position);
                });

                binding.imgEdit.setOnClickListener(V->{
                    listener.onEditClick(position);
                });
            }


            if(isMultiSelect) {
                if (list.get(position).isSelected() || list.get(position).getSpecialitem() == 1) {
                    binding.cbDeal.setChecked(true);
                } else {
                    binding.cbDeal.setChecked(false);
                }
            }else{
                if (list.get(position).isSelected()) {
                    binding.cbDeal.setChecked(true);
                } else {
                    binding.cbDeal.setChecked(false);
                }
            }

            binding.cbDeal.setOnClickListener(V->{
                if(list.get(position).isSelected()){
                    list.get(position).setSelected(false);
                }else{
                    if(!isMultiSelect) {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setSelected(false);
                        }
                    }
                    list.get(position).setSelected(true);
                }
                listener.onEditClick(position);
                notifyDataSetChanged();
            });

        }
    }
}
