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
import com.icashier.app.databinding.ItemCheckoutBinding;
import com.icashier.app.databinding.ItemOrderBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CartItemListener;
import com.icashier.app.model.CartItemModel;
import com.icashier.app.model.MenuResponse;

import java.util.List;

public class CheckoutItemsAdapter extends RecyclerView.Adapter<CheckoutItemsAdapter.ViewHolder>{

    Context context;
    List<CartItemModel> list;
    LayoutInflater inflater;
    public CheckoutItemsAdapter(Context context,List<CartItemModel> list){
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
            if(!list.get(position).getType().equals("meal")){
                Utilities.setCategoryImage(context,binding.imgItem,list.get(position).getImage());
                CheckoutItemExtrasAdapter adapter=new CheckoutItemExtrasAdapter(context,list.get(position).getExtras());
                binding.rvExtras.setAdapter(adapter);
                binding.rvExtras.setLayoutManager(new LinearLayoutManager(context));
                binding.tvItemSize.setText(list.get(position).getSizeAddedToCart());

            }else{
                if(!list.get(position).getImage().equals("")){
                    Utilities.setCategoryImage(context,binding.imgItem,list.get(position).getImage());
                }else {
                    binding.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_meal_item));
                }
                MealItemsAdapter adapter=new MealItemsAdapter(context,list.get(position).getItems(),true);
                binding.rvExtras.setAdapter(adapter);
                binding.rvExtras.setLayoutManager(new LinearLayoutManager(context));
                binding.tvItemSize.setText("_");
            }

            binding.tvItemName.setText(list.get(position).getName());
            binding.tvQuantity.setText(list.get(position).getQtyAddedToCart());


            float selectedPrice=0;
            float discountedPrice=0;
            float totalPrice=0;
            if(list.get(position).getSelectedPrice().equals("")){
                selectedPrice=Float.parseFloat(list.get(position).getSize_price().get(0).getPrice());
            }else{
                selectedPrice=Float.parseFloat(list.get(position).getSelectedPrice());
            }

            if(!list.get(position).getType().equals("meal")){
                if(list.get(position).getSale_percent().equals("")&&list.get(position).getSale_price().equals("")){

                }else{
                    if(!list.get(position).getSale_percent().equals("")){
                        float discount =(float) (selectedPrice * Integer.valueOf(list.get(position).getSale_percent())) / 100;
                        discountedPrice = selectedPrice - discount;
                    }else if(!list.get(position).getSale_price().equals("")){
                        float discount=Integer.valueOf(list.get(position).getSale_price());
                        discountedPrice = selectedPrice - discount;
                    }
                }

                totalPrice=selectedPrice*Integer.parseInt(list.get(position).getQtyAddedToCart());
                discountedPrice=discountedPrice*Integer.parseInt(list.get(position).getQtyAddedToCart());

                for(int i=0;i<list.get(position).getExtras().size();i++){
                    if(list.get(position).getExtras().get(i).isChecked()){
                        totalPrice=totalPrice+(Float.parseFloat(list.get(position).getExtras().get(i).getPrice())*Integer.parseInt(list.get(position).getQtyAddedToCart()));
                        discountedPrice=discountedPrice+(Float.parseFloat(list.get(position).getExtras().get(i).getPrice())*Integer.parseInt(list.get(position).getQtyAddedToCart()));
                    }
                }

                if(!list.get(position).getSale_percent().equals("")||!list.get(position).getSale_price().equals("")){
                    binding.tvItemPrice.setText(""+discountedPrice);
                }else{
                    binding.tvItemPrice.setText(""+totalPrice);
                }
            }else{
               totalPrice= selectedPrice*Integer.parseInt(list.get(position).getQtyAddedToCart());
               binding.tvItemPrice.setText(""+totalPrice);
            }



            if(!list.get(position).getType().equals("meal")){
                int count=0;
                boolean isExtras=false;
                for(int i=0;i<list.get(position).getExtras().size();i++){
                    if(list.get(position).getExtras().get(i).isChecked()){
                        isExtras=true;
                        count++;
                    }
                }

                if(!isExtras){
                    binding.tvDropDown.setVisibility(View.GONE);
                }else{
                    binding.tvDropDown.setVisibility(View.VISIBLE);
                    binding.tvDropDown.setText(""+count);
                }
            }else{
                binding.tvDropDown.setVisibility(View.VISIBLE);
                binding.tvDropDown.setText(""+list.get(position).getItems().size());
            }

            if(list.get(position).isExpanded()){
                binding.rvExtras.setVisibility(View.VISIBLE);
            }else{
                binding.rvExtras.setVisibility(View.GONE);
            }



            binding.tvDropDown.setOnClickListener(V->{
                if(list.get(position).isExpanded()){
                    list.get(position).setExpanded(false);
                }else{
                    list.get(position).setExpanded(true);
                }
                notifyDataSetChanged();
            });
        }
    }
}
