package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.icashier.app.R;
import com.icashier.app.databinding.ItemCheckboxExtraBinding;
import com.icashier.app.model.ExtrasListResponse;

import java.util.List;

public class CheckBoxItemAdapter extends RecyclerView.Adapter<CheckBoxItemAdapter.ViewHolder>{

    Context context;
    List<ExtrasListResponse.ResultBean> extraList;
    LayoutInflater inflater;
    public CheckBoxItemAdapter(Context context, List<ExtrasListResponse.ResultBean> extraList)
    {
        this.context=context;
        this.extraList=extraList;
        this.inflater= LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCheckboxExtraBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_checkbox_extra,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return extraList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ItemCheckboxExtraBinding binding;
        private ViewHolder(ItemCheckboxExtraBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        void bindData(int position)
        {
            if(extraList.get(position).isChecked()){
                binding.cbName.setChecked(true);
            }else
            {
                binding.cbName.setChecked(false);
            }

            binding.cbName.setText(extraList.get(position).getTitle());
            binding.tvPrice.setText("SR "+extraList.get(position).getPrice());

            binding.getRoot().setOnClickListener(V->{
                if(extraList.get(position).isChecked()){
                    extraList.get(position).setChecked(false);
                }else{
                    extraList.get(position).setChecked(true);
                }
                notifyDataSetChanged();
            });
        }
    }
}
