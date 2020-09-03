package com.icashier.app.fragment;

import android.content.DialogInterface;
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
import com.icashier.app.adapter.InvoicesAdapter;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentAllInvoicesBinding;
import com.icashier.app.listener.InvoiceItemListener;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.InvoiceListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AllInvoicesFragment extends Fragment {


    HomeActivity context;
    FragmentAllInvoicesBinding binding;
    RestClient.ApiRequest apiRequest;
    List<InvoiceListResponse.ResultBean> list=new ArrayList<>();
    InvoicesAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity) getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_all_invoices, container, false);
        setAdapter();
        callGetInvoicesApi();
        return binding.getRoot();
    }

    private void setAdapter() {
        adapter=new InvoicesAdapter(context, list, new InvoiceItemListener() {
            @Override
            public void onDeleteClick(int position) {
                AlertUtil.showAlertWindow(context, getString(R.string.delete_invoice_alert), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callDeleteInvoiceApi(position);
                    }
                });
            }
        });
        binding.rvInvoices.setAdapter(adapter);
        binding.rvInvoices.setLayoutManager(new LinearLayoutManager(context));
    }

    //==========================Method to get order list api=====================//
    private void callGetInvoicesApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            //AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.ADD_INVOICE)
                    .setMethod(RestClient.ApiRequest.METHOD_GET)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                InvoiceListResponse invoiceListResponse= new Gson().fromJson(response, InvoiceListResponse.class);
                                if (invoiceListResponse != null) {
                                    if(invoiceListResponse.getCode()==200){
                                        if(invoiceListResponse.getResult().size()>0){
                                         list.addAll(invoiceListResponse.getResult());
                                         adapter.notifyDataSetChanged();
                                        }
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
                            AlertUtil.hideProgressDialog();
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        }else
        {
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
    }

    //==========================Method to get order list api=====================//
    private void callDeleteInvoiceApi(int position) {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("id",""+list.get(position).getId());

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DELETE_INVOICE)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                GenericResponse genericResponse= new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse != null) {
                                    if(genericResponse.getCode()==200){
                                        list.remove(position);
                                        adapter.notifyDataSetChanged();
                                        AlertUtil.toastMsg(context, genericResponse.getMessage());
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
                            AlertUtil.hideProgressDialog();
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
