package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.databinding.ItemMenuExtrasBinding;
import com.icashier.app.model.MenuResponse;

import java.util.List;

public class ItemExtrasAdapter extends RecyclerView.Adapter<ItemExtrasAdapter.ViewHolder>{

    Context context;
    List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean.ExtrasBean> list;
    LayoutInflater inflater;
    String type="";
    public ItemExtrasAdapter(Context context, List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean.ExtrasBean> list,
                             String type){
        this.list=list;
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.type=type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemMenuExtrasBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_menu_extras,parent,false);
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


        ItemMenuExtrasBinding binding;
        public ViewHolder(ItemMenuExtrasBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position)
        {
            if(type.equals(AppConstant.RADIO_BUTTON)){
                binding.clCb.setVisibility(View.GONE);
                binding.clRb.setVisibility(View.VISIBLE);

                if(list.get(position).getType().equals(AppConstant.RADIO_BUTTON)){
                    binding.clRb.setVisibility(View.VISIBLE);
                    if(list.get(position).isChecked()){
                        binding.rbExtra.setChecked(true);
                    }else{
                        binding.rbExtra.setChecked(false);
                    }

                    binding.rbExtra.setText(list.get(position).getTitle());
                    binding.tvPriceRb.setText(list.get(position).getPrice());

                    binding.clRb.setOnClickListener(V->{
                        for(int i=0;i<list.size();i++){
                            if(list.get(i).getType().equals(AppConstant.RADIO_BUTTON)){
                                if(i!=position) {
                                    list.get(i).setChecked(false);
                                }
                            }
                        }
                        if(list.get(position).isChecked()){
                            list.get(position).setChecked(false);
                        }else{
                            list.get(position).setChecked(true);
                        }

                        notifyDataSetChanged();
                    });
                }else{
                    binding.clRb.setVisibility(View.GONE);
                }


            }else if(type.equals(AppConstant.CHECK_BOX)){
                binding.clCb.setVisibility(View.VISIBLE);
                binding.clRb.setVisibility(View.GONE);

                if(list.get(position).getType().equals(AppConstant.CHECK_BOX)){
                    binding.clCb.setVisibility(View.VISIBLE);
                    binding.cbName.setText(list.get(position).getTitle());
                    binding.tvPriceCB.setText(list.get(position).getPrice());

                    if(list.get(position).isChecked()){
                        binding.cbName.setChecked(true);
                    }else{
                        binding.cbName.setChecked(false);
                    }

                    binding.clCb.setOnClickListener(V->{

                        if(list.get(position).isChecked()){
                            list.get(position).setChecked(false);
                        }else{
                            list.get(position).setChecked(true);
                        }

                        notifyDataSetChanged();
                    });
                }else{
                    binding.clCb.setVisibility(View.GONE);
                }
            }




        }
    }
}
