package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.databinding.ItemMenuBinding;
import com.icashier.app.dialog.MealItemsDialog;
import com.icashier.app.dialog.ViewDealDialog;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.MenuListener;
import com.icashier.app.model.MenuResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.ViewHolder>{

    Context context;
    List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> list;
    LayoutInflater inflater;
    MenuListener listener;
    boolean isShowing;
    public MenuItemsAdapter(Context context, List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> list, MenuListener listener){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
        this.listener=listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMenuBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_menu,parent,false);
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

        ItemMenuBinding binding;
        public ViewHolder(ItemMenuBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            this.binding=binding;
        }

        public void bindData(int position){

            if(list.get(position).getType().equals("meal")){
                if(!list.get(position).getImage().equals("")){
                    Utilities.setCategoryImage(context,binding.imgItem,list.get(position).getImage());
                }else{
                    binding.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_meal_item));
                }
                binding.tvPrice.setText("SR "+list.get(position).getSelectedPrice());
                binding.tvPrice.setPaintFlags(binding.tvPrice.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                binding.tvPrice.setTextColor(context.getResources().getColor(R.color.turkishGreen));
                binding.tvDiscountedPrice.setText("");
                binding.tvDiscountedPrice.setVisibility(View.VISIBLE);
                //binding.tvDiscountedPrice.setText(context.getString(R.string.view_items));
                //setCalories(position,binding.tvKCal);
                binding.imgInfo.setOnClickListener(V->{
                    new MealItemsDialog(context,list.get(position).getItems()).show();
                });
            }else  if(list.get(position).getType().equals("deal")){
                if(!list.get(position).getImage().equals("")){
                    Utilities.setCategoryImage(context,binding.imgItem,list.get(position).getImage());
                }else {
                    binding.imgItem.setImageDrawable(context.getResources().getDrawable(R.drawable.deals_icon));
                }
                binding.tvPrice.setText("SR "+list.get(position).getSelectedPrice());
                binding.tvPrice.setPaintFlags(binding.tvPrice.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                binding.tvPrice.setTextColor(context.getResources().getColor(R.color.turkishGreen));
                binding.tvDiscountedPrice.setText("");
                binding.tvDiscountedPrice.setVisibility(View.VISIBLE);
                binding.tvDiscountedPrice.setText(R.string.view_deal);
                //setCalories(position,binding.tvKCal);
                binding.tvDiscountedPrice.setOnClickListener(V->{
                    new ViewDealDialog(context,list.get(position)).show();
                });
                binding.imgInfo.setOnClickListener(V->{
                    List<MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean> itemsBeans=new ArrayList<>();
                    itemsBeans.addAll(list.get(position).getItemsDetails());
                    itemsBeans.addAll(list.get(position).getWithItemsDetails());

                    new MealItemsDialog(context, itemsBeans).show();
                });
            }else {
                Utilities.setCategoryImage(context,binding.imgItem,list.get(position).getImage());
                binding.tvDiscountedPrice.setText("");
                binding.tvDiscountedPrice.setOnClickListener(V->{ });
                float selectedPrice=0;
                float discountedPrice=0;
                float totalPrice=0;
                if(list.get(position).getSelectedPrice().equals("")){
                    selectedPrice=Float.parseFloat(list.get(position).getSize_price().get(0).getPrice());
                }else{
                    selectedPrice=Float.parseFloat(list.get(position).getSelectedPrice());
                }

               /* binding.tvKCal.setVisibility(View.VISIBLE);
                binding.tvKCal.setText("Kcal "+list.get(position).getCalories());*/
                if(list.get(position).getSale_percent().equals("")&&list.get(position).getSale_price().equals("")){

                    binding.tvPrice.setPaintFlags(binding.tvPrice.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    binding.tvPrice.setTextColor(context.getResources().getColor(R.color.turkishGreen));
                }else{
                    binding.tvPrice.setPaintFlags(binding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    binding.tvPrice.setTextColor(context.getResources().getColor(R.color.colorGrey));
                    if(!list.get(position).getSale_percent().equals("")){
                        float discount =(float) (selectedPrice * Integer.valueOf(list.get(position).getSale_percent())) / 100;
                        discountedPrice = selectedPrice - discount;
                    }else if(!list.get(position).getSale_price().equals("")){
                        float discount=Integer.valueOf(list.get(position).getSale_price());
                        discountedPrice = selectedPrice - discount;
                    }
                }

                totalPrice=selectedPrice*list.get(position).getCount();
                discountedPrice=discountedPrice*list.get(position).getCount();



                for(int i=0;i<list.get(position).getExtras().size();i++){
                    if(list.get(position).getExtras().get(i).isChecked()){
                        totalPrice=totalPrice+(Float.parseFloat(list.get(position).getExtras().get(i).getPrice())*list.get(position).getCount());
                        discountedPrice=discountedPrice+(Float.parseFloat(list.get(position).getExtras().get(i).getPrice())*list.get(position).getCount());
                    }
                }

                binding.tvPrice.setText("SR "+totalPrice);

                if(!list.get(position).getSale_price().equals("")||!list.get(position).getSale_percent().equals("")){
                    discountedPrice=(float)(Math.round(discountedPrice * 100.0) / 100.0);
                    binding.tvDiscountedPrice.setText("SR "+discountedPrice);
                }else{
                    binding.tvDiscountedPrice.setText("");
                }


                binding.imgInfo.setOnClickListener(V->{
                    new MealItemsDialog(context, Arrays.asList(new MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean[] {list.get(position)})).show();
                });
            }
            String appLang= SharedPrefManager.getInstance(context).getString(AppConstant.APP_LANGUAGE,"en");
            if (appLang.equals("ar")){
                if (list.get(position).getTitleAr()!=null  && !list.get(position).getTitleAr().isEmpty()){
                    binding.tvItemName.setText(list.get(position).getTitleAr());
                }else{
                    binding.tvItemName.setText(list.get(position).getName());
                }
            }else{
                binding.tvItemName.setText(list.get(position).getName());
            }
            binding.tvLikeCount.setText(""+list.get(position).getNumbers());


            if(list.get(position).isSelected()){
                binding.imgAddItem.setSelected(true);
            }else{
                binding.imgAddItem.setSelected(false);
            }

            if(list.get(position).isPopupShowing()){
                binding.tvDropDown.setVisibility(View.VISIBLE);
            }else {
                binding.tvDropDown.setVisibility(View.GONE);
            }
            //0 means available and 1 means sold out
            if(list.get(position).getSold()==0){
                binding.tvSoldOut.setVisibility(View.GONE);
                binding.tvDropDown.setText(context.getString(R.string.sold_out));


            }else if(list.get(position).getSold()==1){
               // binding.tvSoldOut.setOnClickListener(V->{});
                binding.tvSoldOut.setVisibility(View.VISIBLE);
                binding.tvDropDown.setText(R.string.in_stock);
            }

            binding.clLay.setOnClickListener(V->{

                for(int i=0;i<list.size();i++){
                    if(i!=position){
                        list.get(i).setSelected(false);
                    }
                }
                if(!list.get(position).isSelected()){
                    list.get(position).setSelected(true);
                    listener.onItemSelected(position);
                }else{
                    list.get(position).setSelected(false);
                    listener.onItemSelected(-1);
                }
                list.get(position).setPopupShowing(false);
                notifyDataSetChanged();
            });

            binding.clLay.setOnLongClickListener(v -> {
                showSoldOut(position,v);
                return false;
            });

            binding.clParentLayout.setOnClickListener(V->{
                list.get(position).setPopupShowing(false);
                notifyDataSetChanged();

            });

            binding.imgDropDown.setOnClickListener(V->{
                for(int i=0;i<list.size();i++){
                    if(i!=position){
                        list.get(i).setPopupShowing(false);
                    }
                }
                if(list.get(position).isPopupShowing()){
                    list.get(position).setPopupShowing(false);
                }else{
                    list.get(position).setPopupShowing(true);
                }

                notifyDataSetChanged();
            });

            binding.tvDropDown.setOnClickListener(V->{
                listener.onStockChanged(position);
            });

        }
    }

    /*
    method to set calories for deal and meal
     */
    private void setCalories(int position, TextView view) {
        float calories=0;
        if(list.get(position).getType().equals(AppConstant.TYPE_DEAL)){
            if(list.get(position).getItemsDetails()!=null){
                for(MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean item:list.get(position).getItemsDetails()){
                    calories=calories+Float.parseFloat(item.getCalories());
                }
            }
            if(list.get(position).getWithItemsDetails()!=null){
                for(MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean item:list.get(position).getWithItemsDetails()){
                    calories=calories+Float.parseFloat(item.getCalories());
                }
            }

        }else if(list.get(position).getType().equals(AppConstant.TYPE_MEAL)){
            if(list.get(position).getItems()!=null){
                for(MenuResponse.MerchantBean.CategoriesBean.SubCategoriesBean.ItemsBean item:list.get(position).getItems()){
                    calories=calories+Float.parseFloat(item.getCalories());
                }
            }
        }
        view.setText("Kcal "+calories);
    }

    //======================Mehtod to show primary category list popup=======================//
    private void showSoldOut(int pos,View view) {

        List<String> strList=new ArrayList<>();

        if(list.get(pos).getSold()==1){
            strList.add(context.getString(R.string.in_stock));
        }else{
            strList.add(context.getString(R.string.sold_out));
        }
        ArrayAdapter listPopupAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, strList);

        ListPopupWindow popupWindow = new ListPopupWindow(context);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int index, long id) {

                listener.onStockChanged(pos);
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentWidth(view.getWidth());
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setAnchorView(view);
        popupWindow.setAdapter(listPopupAdapter);
        popupWindow.show();

    }

}
