package com.icashier.app.adapter;

import android.content.ClipData;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.databinding.ItemCheckoutBinding;
import com.icashier.app.databinding.ItemOrderBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CartItemListener;
import com.icashier.app.model.CartItemModel;
import com.icashier.app.model.MenuResponse;
import com.icashier.app.model.OrderListResponse;

import java.util.ArrayList;
import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder>{

    Context context;
    List<OrderListResponse.ResultBean.ItemsBean> list;
    LayoutInflater inflater;
    public OrderItemsAdapter(Context context,List<OrderListResponse.ResultBean.ItemsBean> list){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCheckoutBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_checkout,parent,false);
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

        ItemCheckoutBinding binding;
        public ViewHolder(ItemCheckoutBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){
            if(list.get(position).getType()!=null) {
                if (list.get(position).getType().equals("meal")) {
                    if (!list.get(position).getImage().equals("")) {
                        Utilities.setCategoryImage(context, binding.imgItem, list.get(position).getImage());
                    } else {
                        binding.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_meal_item));
                    }
                    binding.tvItemName.setText(list.get(position).getTitle());
                    OrderMealItemsAdapter adapter = new OrderMealItemsAdapter(context, list.get(position).getItems());
                    binding.rvExtras.setAdapter(adapter);
                    binding.rvExtras.setLayoutManager(new LinearLayoutManager(context));

                    binding.tvItemSize.setText(list.get(position).getSizeAddedToCart());

                    if (list.get(position).getItems().size() > 0) {
                        binding.tvDropDown.setText("" + list.get(position).getItems().size());
                        binding.tvDropDown.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvDropDown.setVisibility(View.GONE);
                    }


                } else if (list.get(position).getType().equals("deal")) {
                    if (!list.get(position).getImage().equals("")) {
                        Utilities.setCategoryImage(context, binding.imgItem, list.get(position).getImage());
                    } else {
                        binding.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.deals_icon));
                    }
                    binding.tvItemName.setText(list.get(position).getTitle());
                    //OrderMealItemsAdapter adapter=new OrderMealItemsAdapter(context,list.get(position).getItems());
                    // binding.rvExtras.setAdapter(adapter);
                    // binding.rvExtras.setLayoutManager(new LinearLayoutManager(context));
                    binding.tvItemSize.setText("_");

                    List<OrderListResponse.ResultBean.ItemsBean> itemsBeans=new ArrayList<>();
                    if(list.get(position).getItemsDetails()!=null)
                    itemsBeans.addAll(list.get(position).getItemsDetails());

                    if(list.get(position).getWithItemsDetails()!=null)
                    itemsBeans.addAll(list.get(position).getWithItemsDetails());

                    OrderMealItemsAdapter adapter = new OrderMealItemsAdapter(context,itemsBeans);
                    binding.rvExtras.setAdapter(adapter);
                    binding.rvExtras.setLayoutManager(new LinearLayoutManager(context));
                    if (itemsBeans.size() > 0) {
                        binding.tvDropDown.setText("" + itemsBeans.size());
                        binding.tvDropDown.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvDropDown.setVisibility(View.GONE);
                    }


                } else {
                    Utilities.setCategoryImage(context, binding.imgItem, list.get(position).getImage());
                    OrderItemExtrasAdapter adapter = new OrderItemExtrasAdapter(context, list.get(position).getExtrasAddedToCart());
                    binding.rvExtras.setAdapter(adapter);
                    binding.rvExtras.setLayoutManager(new LinearLayoutManager(context));
                    if(list.get(position).getSizeAddedToCart().equalsIgnoreCase(AppConstant.DEFAULT_SIZE)){
                        binding.tvItemSize.setText(context.getString(R.string.default_size));
                    }else{
                        binding.tvItemSize.setText(list.get(position).getSizeAddedToCart());
                    }

                    if (list.get(position).getExtrasAddedToCart() != null && list.get(position).getExtrasAddedToCart().size() > 0) {
                        binding.tvDropDown.setText("" + list.get(position).getExtrasAddedToCart().size());
                        binding.tvDropDown.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvDropDown.setVisibility(View.GONE);
                    }
                    binding.tvItemName.setText(list.get(position).getName());
                }


                binding.tvQuantity.setText(list.get(position).getQtyAddedToCart());
                binding.tvItemPrice.setText(list.get(position).getPriceForItem());

                if (list.get(position).getReorder_count() > 0) {
                    binding.tvReorderNo.setVisibility(View.VISIBLE);
                    binding.tvReorderNo.setText("" + list.get(position).getReorder_count());
                } else {
                    binding.tvReorderNo.setVisibility(View.GONE);
                }


                if (list.get(position).isIsExpanded()) {
                    binding.rvExtras.setVisibility(View.VISIBLE);
                } else {
                    binding.rvExtras.setVisibility(View.GONE);
                }


                binding.tvDropDown.setOnClickListener(V -> {
                    if (list.get(position).isIsExpanded()) {
                        list.get(position).setIsExpanded(false);
                    } else {
                        list.get(position).setIsExpanded(true);
                    }
                    notifyDataSetChanged();
                });
            }
        }
    }
}
