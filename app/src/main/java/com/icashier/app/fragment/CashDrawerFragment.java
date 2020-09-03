package com.icashier.app.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.SalesListAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentCashDrawerBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.OrderListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CashDrawerFragment extends Fragment {

    FragmentCashDrawerBinding binding;
    HomeActivity context;
    RestClient.ApiRequest apiRequest;
    List<OrderListResponse.ResultBean> orderList=new ArrayList<>();
    SalesListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_cash_drawer, container, false);
        setAdapter();
        callGetOrdersApi();
        return binding.getRoot();
    }

    //==============Method to sales list adpater==========//
    private void setAdapter() {
        adapter=new SalesListAdapter(context,orderList);
        binding.rvSales.setAdapter(adapter);
        binding.rvSales.setLayoutManager(new LinearLayoutManager(context));

    }

    //================Method to get orders list api============//
    private void callGetOrdersApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_ORDER_LIST)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setHeader2("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {

                                OrderListResponse orderListResponse= new Gson().fromJson(response, OrderListResponse.class);
                                if (orderListResponse != null) {
                                    if(orderListResponse.getCode()==200){
                                        orderList.addAll(orderListResponse.getResult());
                                        adapter.notifyDataSetChanged();

                                    }else{
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
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
                            binding.progressBar.setVisibility(View.GONE);
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        }else
        {
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
    }

}
