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
import com.icashier.app.databinding.ItemOrderBinding;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CartItemListener;
import com.icashier.app.model.CartItemModel;
import com.icashier.app.model.MenuResponse;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    Context context;
    List<CartItemModel> list;
    LayoutInflater inflater;
    CartItemListener listener;
    public CartAdapter(Context context,List<CartItemModel> list,CartItemListener listener){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_order,parent,false);
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

        ItemOrderBinding binding;
        public ViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){
            if(position==list.size()-1){
                binding.bottomView.setVisibility(View.VISIBLE);
            }else{
                binding.bottomView.setVisibility(View.GONE);
            }
            if(list.get(position).getType().equals("meal")){
                if(!list.get(position).getImage().equals("")){
                    Utilities.setCategoryImage(context,binding.imgItem,list.get(position).getImage());
                }else {
                    binding.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_meal_item));
                }
                binding.tvSize.setVisibility(View.GONE);
                binding.tvSizeLabel.setVisibility(View.GONE);
                binding.btnExtras.setVisibility(View.GONE);
                binding.btnItems.setVisibility(View.VISIBLE);
                MealItemsAdapter adapter=new MealItemsAdapter(context,list.get(position).getItems(),false);
                binding.rvExtras.setAdapter(adapter);
                binding.rvExtras.setLayoutManager(new LinearLayoutManager(context));
                binding.tvEmptyView.setVisibility(View.GONE);


            }else if(list.get(position).getType().equals("deal")){

                if(!list.get(position).getImage().equals("")){
                    Utilities.setCategoryImage(context,binding.imgItem,list.get(position).getImage());
                }else {
                    binding.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.deals_icon));
                }
                binding.tvSize.setVisibility(View.GONE);
                binding.tvSizeLabel.setVisibility(View.GONE);
                binding.btnExtras.setVisibility(View.GONE);
                binding.btnItems.setVisibility(View.VISIBLE);
                List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> itemsBeans=new ArrayList<>();
                itemsBeans.addAll(list.get(position).getItemsDetails());
                itemsBeans.addAll(list.get(position).getWithItemsDetails());
                MealItemsAdapter adapter=new MealItemsAdapter(context,itemsBeans,false);
                binding.rvExtras.setAdapter(adapter);
                binding.rvExtras.setLayoutManager(new LinearLayoutManager(context));
                binding.tvEmptyView.setVisibility(View.GONE);

            }else{
                Utilities.setCategoryImage(context,binding.imgItem,list.get(position).getImage());
                binding.tvSize.setVisibility(View.VISIBLE);
                binding.tvSizeLabel.setVisibility(View.VISIBLE);
                if(list.get(position).getSizeAddedToCart().equalsIgnoreCase(AppConstant.DEFAULT_SIZE)){
                    binding.tvSize.setText(context.getString(R.string.default_size));

                }else{
                    binding.tvSize.setText(list.get(position).getSizeAddedToCart());
                }
                binding.btnExtras.setVisibility(View.VISIBLE);
                binding.btnItems.setVisibility(View.GONE);
                CartExrasAdapter adapter=new CartExrasAdapter(context,list.get(position).getExtras());
                binding.rvExtras.setAdapter(adapter);
                binding.rvExtras.setLayoutManager(new LinearLayoutManager(context));
                boolean showExtraBtn=false;
                if(list!=null)
                for(int i=0;i<list.get(position).getExtras().size();i++){
                    if(list.get(position).getExtras().get(i).isChecked()){
                        showExtraBtn=true;
                        break;
                    }
                }
                if(showExtraBtn){
                    binding.btnExtras.setVisibility(View.VISIBLE);
                }else{
                    binding.btnExtras.setVisibility(View.GONE);
                }

            }

            binding.tvItemName.setText(list.get(position).getName());


            binding.tvCount.setText(list.get(position).getQtyAddedToCart());


            float selectedPrice=0;
            float discountedPrice=0;
            float totalPrice=0;
            if(list.get(position).getSelectedPrice().equals("")){
                selectedPrice=Float.parseFloat(list.get(position).getSize_price().get(0).getPrice());
            }else{
                selectedPrice=Float.parseFloat(list.get(position).getSelectedPrice());
            }

            if(!list.get(position).getType().equals("meal")&&!list.get(position).getType().equals("deal")){
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
                    discountedPrice=(float)(Math.round(discountedPrice * 100.0) / 100.0);
                    binding.tvPrice.setText("SR "+discountedPrice);
                    list.get(position).setPriceForItem(""+discountedPrice);
                }else{
                    binding.tvPrice.setText("SR "+totalPrice);
                    list.get(position).setPriceForItem(""+totalPrice);

                }
            }else{
               totalPrice= selectedPrice*Integer.parseInt(list.get(position).getQtyAddedToCart());
               binding.tvPrice.setText("SR "+totalPrice);
                list.get(position).setPriceForItem(""+totalPrice);
            }



            if(list.get(position).isExpanded()){
                binding.clExtras.setVisibility(View.VISIBLE);

            }else{
                binding.clExtras.setVisibility(View.GONE);
            }

            if(!list.get(position).getType().equals("meal")&&!list.get(position).getType().equals("deal")){
                boolean isExtras=false;
                for(int i=0;i<list.get(position).getExtras().size();i++){
                    if(list.get(position).getExtras().get(i).isChecked()){
                        isExtras=true;
                        break;
                    }
                }

                if(!isExtras){
                    binding.tvEmptyView.setVisibility(View.VISIBLE);
                }else{
                    binding.tvEmptyView.setVisibility(View.GONE);
                }
            }


            if(list.get(position).isCartPopupShowing()){
                binding.llPopup.setVisibility(View.VISIBLE);
            }else{
                binding.llPopup.setVisibility(View.GONE);
            }

            if(list.get(position).isExpanded()){
                binding.btnItems.setSelected(true);
                binding.btnExtras.setSelected(true);
            }else{
                binding.btnItems.setSelected(false);
                binding.btnExtras.setSelected(false);
            }

            if(list.get(position).isSelected()){
                binding.overlay.setVisibility(View.GONE);
                binding.overlay.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                binding.btnExtras.setOnClickListener(V->{

                    for(int i=0;i<list.size();i++){
                        list.get(position).setCartPopupShowing(false);
                    }
                    if(list.get(position).isExpanded()){
                        list.get(position).setExpanded(false);
                    }else{
                        list.get(position).setExpanded(true);
                    }

                    notifyDataSetChanged();
                });

                binding.btnItems.setOnClickListener(V->{

                    for(int i=0;i<list.size();i++){
                        list.get(position).setCartPopupShowing(false);
                    }
                    if(list.get(position).isExpanded()){
                        list.get(position).setExpanded(false);
                    }else{
                        list.get(position).setExpanded(true);
                    }

                    notifyDataSetChanged();
                });

                binding.imgDropDown.setOnClickListener(V->{
                    for(int i=0;i<list.size();i++){
                        if(i!=position) {
                            list.get(position).setCartPopupShowing(false);
                        }
                    }

                    if(list.get(position).isCartPopupShowing()){
                        list.get(position).setCartPopupShowing(false);
                    }else{
                        list.get(position).setCartPopupShowing(true);
                    }
                    notifyDataSetChanged();
                });

            }else{
                binding.overlay.setVisibility(View.VISIBLE);
                binding.overlay.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, binding.clParentLayout.getHeight()));
                binding.imgDropDown.setOnClickListener(V->{});
                binding.btnExtras.setOnClickListener(V->{});
                binding.btnItems.setOnClickListener(V->{});
            }



            binding.clParentLayout.setOnClickListener(V->{
                for(int i=0;i<list.size();i++){
                    list.get(position).setCartPopupShowing(false);
                }
                notifyDataSetChanged();
            });


            binding.llLay.setOnClickListener(V->{
                listener.onItemEdit(position);
                for(int i=0;i<list.size();i++){
                    if(i!=position){
                        list.get(i).setSelected(false);
                    }else{
                        list.get(i).setSelected(true);
                    }
                }
                list.get(position).setCartPopupShowing(false);
                notifyDataSetChanged();
            });

            binding.tvRemove.setOnClickListener(V->{
                list.get(position).setCartPopupShowing(false);
                list.remove(position);
                listener.onItemRemoved(position);
                notifyDataSetChanged();
            });


        }
    }
}
