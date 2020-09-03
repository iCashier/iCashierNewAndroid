package com.icashier.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.databinding.ItemOrderBinding;
import com.icashier.app.databinding.ItemOrdersBinding;
import com.icashier.app.dialog.AutomaticOrderDetailDialog;
import com.icashier.app.dialog.OrderDetailDialog;
import com.icashier.app.helper.DateUtils;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.listener.OkClickListener;
import com.icashier.app.model.OrderListResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.icashier.app.helper.DateUtils.convertUTCtoLocalTime;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder>{

    Context context;
    List<OrderListResponse.ResultBean> list;
    LayoutInflater inflater;
    OkClickListener listener;

    public OrderListAdapter(Context context, List<OrderListResponse.ResultBean> list,OkClickListener listener){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrdersBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_orders,parent,false);
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

        ItemOrdersBinding binding;
        public ViewHolder(ItemOrdersBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){

            if(!list.get(position).getTableName().equalsIgnoreCase("null")&&!list.get(position).getTableName().equals("")){
                binding.tvOrderNo.setText("#"+list.get(position).getSequence_no()+" - "+list.get(position).getTableName());
            }else {
                binding.tvOrderNo.setText("#" + list.get(position).getSequence_no());
            }

            setDateTime(position);
          //  autoPrint();
            if(list.get(position).getDelivery().equals(AppConstant.TYPE_PICKUP)){
                binding.imgDelivery.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_takeout));
                binding.tvStatusLabel.setVisibility(View.GONE);
                binding.tvStatus.setVisibility(View.GONE);
            }else if(list.get(position).getDelivery().equals(AppConstant.TYPE_DINE_IN)){
                binding.imgDelivery.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_dine_in));
                binding.tvStatusLabel.setVisibility(View.GONE);
                binding.tvStatus.setVisibility(View.GONE);

            }else if(list.get(position).getDelivery().equals(AppConstant.TYPE_DELIVERY)){
                binding.imgDelivery.setImageDrawable(context.getResources().getDrawable(R.drawable.delivery));
                binding.tvStatusLabel.setVisibility(View.VISIBLE);
                binding.tvStatus.setVisibility(View.VISIBLE);

            }else if(list.get(position).getDelivery().equals(AppConstant.TYPE_SWIFT_DELIVERY)){
                binding.imgDelivery.setImageDrawable(context.getResources().getDrawable(R.drawable.swift_delivery));
                binding.tvStatusLabel.setVisibility(View.VISIBLE);
                binding.tvStatus.setVisibility(View.VISIBLE);
            }else if(list.get(position).getDelivery().equals(AppConstant.TYPE_INSTA_DELIVERY)){
                binding.imgDelivery.setImageDrawable(context.getResources().getDrawable(R.drawable.insta_delivery));
                binding.tvStatusLabel.setVisibility(View.VISIBLE);
                binding.tvStatus.setVisibility(View.VISIBLE);

            }else if(list.get(position).getDelivery().equals(AppConstant.CASHIER)){
                binding.imgDelivery.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_dine_in));
                list.get(position).setStatus(AppConstant.STATUS_DELIVERED);
                binding.tvStatusLabel.setVisibility(View.GONE);
                binding.tvStatus.setVisibility(View.GONE);
            }

            if(list.get(position).getPayment().equalsIgnoreCase(AppConstant.CASH)){
                binding.imgPaymentMode.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_cash_staus));
            }else if(list.get(position).getPayment().equalsIgnoreCase(AppConstant.CREDIT_CARD)){
                binding.imgPaymentMode.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_card_status));
            }else {
                binding.imgPaymentMode.setImageDrawable(context.getResources().getDrawable(R.drawable.bg_card_status));
            }

            if(list.get(position).getPaymentStatus()==1){
                binding.imgPaymentMode.setSelected(true);
            }else{
                binding.imgPaymentMode.setSelected(false);

            }

            if(list.get(position).getStatus().equalsIgnoreCase(AppConstant.STATUS_PENDING)){

               // binding.tvStatus.setText(R.string.pending);
                binding.tvStatus.setText("");
                binding.tvStatus.setTextColor(context.getResources().getColor(R.color.pendingColor));
                binding.viewRed.setVisibility(View.VISIBLE);

            }else if(list.get(position).getStatus().equalsIgnoreCase(AppConstant.STATUS_PROCESSING)){
                binding.tvStatus.setText(R.string.proccesing);
                binding.tvStatus.setTextColor(context.getResources().getColor(R.color.processingColor));
                binding.viewRed.setVisibility(View.GONE);
            }else if(list.get(position).getStatus().equalsIgnoreCase(AppConstant.STATUS_DELIVERED)){
                binding.tvStatus.setText(R.string.delivered);
                binding.tvStatus.setTextColor(context.getResources().getColor(R.color.deliveredColor));
                binding.viewRed.setVisibility(View.GONE);
            }else{
                binding.viewRed.setVisibility(View.GONE);
            }

            if(list.get(position).getWaiterCall()==1){
                binding.clWaiterCalling.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_waiter_calling));
                binding.clWaiterCalling.setVisibility(View.VISIBLE);
                binding.tvWaiter.setText(context.getString(R.string.waiter_call));
                if(!list.get(position).getTableName().equalsIgnoreCase("null")&&!list.get(position).getTableName().equals("")){
                    binding.tvOrderId.setText("#"+list.get(position).getSequence_no()+" - "+list.get(position).getTableName());
                }else {
                    binding.tvOrderId.setText("#" + list.get(position).getSequence_no());
                }
            }else if(list.get(position).getWaiterCall()==2){
                binding.clWaiterCalling.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_payment_req));
                binding.clWaiterCalling.setVisibility(View.VISIBLE);
                binding.tvWaiter.setText(context.getString(R.string.payment_req));
                if(!list.get(position).getTableName().equalsIgnoreCase("null")&&!list.get(position).getTableName().equals("")){
                    binding.tvOrderId.setText("#"+list.get(position).getSequence_no()+" - "+list.get(position).getTableName());
                }else {
                    binding.tvOrderId.setText("#" + list.get(position).getSequence_no());
                }
            }else{
                binding.clWaiterCalling.setVisibility(View.GONE);
            }

            binding.btnOk.setOnClickListener(V->{
                listener.onOKClicked(position);
            });

            if(list.get(position).getIsReordered()==1&&list.get(position).getDelivery().equals(AppConstant.TYPE_DINE_IN)){
                binding.clOrder.setSelected(true);
            }else{
                binding.clOrder.setSelected(false);
            }


            binding.getRoot().setOnClickListener(V->{
                String s=list.get(position).toString();
                Log.e("PrintDialog Response",s);
                OrderDetailDialog orderDetailDialog= new OrderDetailDialog((HomeActivity) context,list.get(position));
                orderDetailDialog.show();
                orderDetailDialog.setOnDismissListener(D->{
                    notifyDataSetChanged();
                });
            });

        }


        private void setDateTime(int position) {

            String date=list.get(position).getDate();

            try {
                date=convertUTCtoLocalTime(date);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String[] dateArray=date.split(" ");

            if(DateUtils.isDaysAgo(date,binding.tvTime,context)){

            } else{
                String[] formatDate=dateArray[0].split("-");
                binding.tvTime.setText(formatDate[2]+"/"+formatDate[1]+"/"+formatDate[0]);
            }



        }
    }


}
