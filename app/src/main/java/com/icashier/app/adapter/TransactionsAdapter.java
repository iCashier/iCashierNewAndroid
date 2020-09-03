package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.databinding.ItemTransactionsBinding;
import com.icashier.app.helper.DateUtils;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.RefundStatusListener;
import com.icashier.app.model.OrderListResponse;

import java.util.ArrayList;
import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> implements Filterable {

    Context context;
    List<OrderListResponse.ResultBean> list;
    List<OrderListResponse.ResultBean> tempList;
    LayoutInflater inflater;
    RefundStatusListener listener;
    String search="";

    public TransactionsAdapter(Context context,List<OrderListResponse.ResultBean> list,RefundStatusListener listener){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionsBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_transactions,parent,false);
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



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //here constraint charSequence which is type in search bar
                FilterResults results = new FilterResults();
                List<OrderListResponse.ResultBean> FilteredArrList = new ArrayList<>();
                if (tempList == null) {
                    tempList = new ArrayList<>(list);
                    // saves the original data in contactDataListOriginal

                }
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = tempList.size();
                    results.values = tempList;
                } else {
                    search= (String) constraint;
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < tempList.size(); i++) {
                        OrderListResponse.ResultBean data = tempList.get(i);

                        if (String.valueOf(data.getSequence_no()).toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(data);
                        }

                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<OrderListResponse.ResultBean>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemTransactionsBinding binding;
        public ViewHolder(ItemTransactionsBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bindData(int position){
            try {
                binding.tvDate.setText(DateUtils.convertUTCtoLocalTime2(list.get(position).getDate(),"MMM dd, yyyy"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            binding.tvReceipt.setText(""+list.get(position).getSequence_no());
            float totalPrice=(float)(Math.round(Double.parseDouble(list.get(position).getTotal()) * 100.0) / 100.0);

            binding.tvPrice.setText(""+totalPrice);
            if(list.get(position).getPayment().equalsIgnoreCase(AppConstant.CASH)){
                binding.tvPayment.setText(context.getString(R.string.cash));
                binding.tvPayment.setSelected(true);
            }else if(list.get(position).getPayment().equalsIgnoreCase(AppConstant.CREDIT_CARD)){
                binding.tvPayment.setText(context.getString(R.string.card));
                binding.tvPayment.setSelected(false);
            }else {
                binding.tvPayment.setText(context.getString(R.string.online));
                binding.tvPayment.setSelected(false);
            }

            if(list.get(position).isExpanded()){
                binding.llItems.setVisibility(View.VISIBLE);
                binding.imgPlusMinus.setSelected(true);
            }else{
                binding.llItems.setVisibility(View.GONE);
                binding.imgPlusMinus.setSelected(false);
            }

            List<OrderListResponse.ResultBean.ItemsBean> itemList=new ArrayList<>();
            itemList.addAll(list.get(position).getItems());
            itemList.addAll(list.get(position).getMeal());
            itemList.addAll(list.get(position).getDeals());

            OrderItemsAdapter adapter=new OrderItemsAdapter(context,itemList);
            binding.rvItems.setLayoutManager(new LinearLayoutManager(context));
            binding.rvItems.setAdapter(adapter);

            if(!list.get(position).isRefund()){
                binding.imgRefund.setColorFilter(null);

                binding.imgRefund.setOnClickListener(V->{
                    listener.onRefund(position);
                });
            }else{
                binding.imgRefund.setColorFilter(context.getResources().getColor(R.color.lightGreen));
                binding.imgRefund.setOnClickListener(V->{});
            }


            binding.tvSubTotal.setText("SR "+ Utilities.formatPrice(Float.parseFloat(list.get(position).getSubtotal())) );
            binding.tvTax.setText("SR "+Utilities.formatPrice(Float.parseFloat(list.get(position).getTax())));



            binding.tvTotal.setText("SR "+Utilities.formatPrice(totalPrice));

            if(list.get(position).getCustomAmount()>0) {
                binding.clCustomAmount.setVisibility(View.VISIBLE);
                binding.tvCustomAmount.setText("SR "+Utilities.formatPrice(list.get(position).getCustomAmount()));
            }else{
                binding.clCustomAmount.setVisibility(View.GONE);
            }
            binding.delCharges.setVisibility(View.GONE);


            binding.imgPlusMinus.setOnClickListener(V->{
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
