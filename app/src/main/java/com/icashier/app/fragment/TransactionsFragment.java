package com.icashier.app.fragment;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.TransactionsAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentTransactionsBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.RefundStatusListener;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.OrderListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TransactionsFragment extends Fragment {

    HomeActivity context;
    FragmentTransactionsBinding binding;
    RestClient.ApiRequest apiRequest;
    TransactionsAdapter adapter;
    List<OrderListResponse.ResultBean> list=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_transactions, container, false);
        binding.tabDinein.setSelected(true);
        setAdapter();
        callGetOrdersApi();
        return binding.getRoot();
    }

    private void setAdapter() {
        adapter=new TransactionsAdapter(context, list, new RefundStatusListener() {
            @Override
            public void onRefund(int position) {
                AlertUtil.showAlertWindow(context, getString(R.string.change_refund_status_alert), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callChangeStatusApi(position);
                    }
                });
            }
        });
        binding.rvTransactions.setAdapter(adapter);
        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(context));

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s.toString());
            }
        });
    }

    //==========================Method to get order list api=====================//
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
                                        list.addAll(orderListResponse.getResult());
                                        adapter.notifyDataSetChanged();
                                    }else{

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

    private void callChangeStatusApi(int position) {
        if(Utilities.isNetworkAvailable(context)) {
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("id", ""+list.get(position).getId());
            params.put("refund","true");


            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.CHANGE_ORDER_STATUS)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    GenericResponse genericResponse= new Gson().fromJson(response, GenericResponse.class);
                                    if (genericResponse != null) {
                                        if (genericResponse.getCode()==200) {
                                            list.get(position).setRefund(true);
                                            adapter.notifyDataSetChanged();
                                        }else if(genericResponse.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    AlertUtil.showAlertWindow(context, context.getString(R.string.still_want_to_proceed), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            callChangeStatusApi(position);
                                                        }
                                                    });
                                                }
                                            });
                                        }

                                    } else {
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            AlertUtil.hideProgressDialog();
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.network_error));
        }
    }

}
