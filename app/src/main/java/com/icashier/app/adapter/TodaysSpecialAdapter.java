package com.icashier.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogTodaySpecialBinding;
import com.icashier.app.databinding.ItemTodaySpecialBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.model.ExistingItemList;
import com.icashier.app.model.GenericResponse;

import java.util.HashMap;
import java.util.List;

public class TodaysSpecialAdapter extends RecyclerView.Adapter<TodaysSpecialAdapter.ViewHolder> {

    Context context;
    List<ExistingItemList.ResultBean> existingItemList;
    LayoutInflater inflater;
    RestClient.ApiRequest apiRequest;
    DialogTodaySpecialBinding dialogBinding;

    public TodaysSpecialAdapter(Context context,List<ExistingItemList.ResultBean> existingItemList,DialogTodaySpecialBinding dialogBinding){
        this.existingItemList=existingItemList;
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.dialogBinding=dialogBinding;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTodaySpecialBinding binding= DataBindingUtil.inflate(inflater, R.layout.item_today_special,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return existingItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ItemTodaySpecialBinding binding;
        public ViewHolder(ItemTodaySpecialBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        void bindData(int position){
            binding.tvItemName.setText(existingItemList.get(position).getName());
            binding.tvItemSize.setText(existingItemList.get(position).getSize().replace(AppConstant.DEFAULT_SIZE,context.getString(R.string.default_size)));
            binding.tvItemPrice.setText(existingItemList.get(position).getPrice());
            if(Integer.parseInt(existingItemList.get(position).getQty())<0) {
                binding.tvQuantity.setText(R.string.unlimited);

            }else{
                binding.tvQuantity.setText(existingItemList.get(position).getQty());
            }

            Utilities.setCategoryImage(context,binding.imgItem,ServerConstants.IMAGE_BASE_URL+existingItemList.get(position).getImage());

            if(existingItemList.get(position).getSpecialitem()==1){
                binding.cbItem.setChecked(true);
            }else if(existingItemList.get(position).getSpecialitem()==0){
                binding.cbItem.setChecked(false);
            }

            binding.cbItem.setOnClickListener(V->{

                callEditItemApi(position);
            });

        }

        private void callEditItemApi(int position) {

            if (Utilities.isNetworkAvailable(context)) {
                dialogBinding.progressBar.setVisibility(View.VISIBLE);

                HashMap<String, String> params = new HashMap<>();
                params.put("id",""+existingItemList.get(position).getId());
                if(existingItemList.get(position).getSpecialitem()==1){
                    params.put("specialitem", "0");

                }else{
                    params.put("specialitem","1");
                }

                apiRequest=new RestClient.ApiRequest(context);
                apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.EDIT_ITEM)
                        .setMethod(RestClient.ApiRequest.METHOD_MULTIPART)
                        .setParams(params)
                        .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN, ""))
                        .setResponseListener(new RestClient.ResponseListener() {
                            @Override
                            public void onResponse(String tag, String response) {
                                dialogBinding.progressBar.setVisibility(View.GONE);
                                if (Utilities.isValidJson(response)) {
                                    GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                    if (genericResponse != null) {
                                        if (genericResponse.getCode() == 200) {
                                            if(existingItemList.get(position).getSpecialitem()==1){
                                                existingItemList.get(position).setSpecialitem(0);
                                            }else if(existingItemList.get(position).getSpecialitem()==0){
                                                existingItemList.get(position).setSpecialitem(1);
                                            }
                                            notifyDataSetChanged();
                                            AlertUtil.toastMsg(context, genericResponse.getMessage());
                                        } else if(genericResponse.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callEditItemApi(position);
                                                }
                                            });
                                        }
                                    } else {
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
                                }
                            }
                        })
                        .setErrorListener(new RestClient.ErrorListener() {
                            @Override
                            public void onError(String tag, String errorMsg) {
                                dialogBinding.progressBar.setVisibility(View.GONE);
                                AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                            }
                        })
                        .execute();
            } else {
                AlertUtil.toastMsg(context, context.getString(R.string.network_error));
            }
        }


    }


}
