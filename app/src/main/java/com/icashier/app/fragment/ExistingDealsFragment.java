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
import com.icashier.app.adapter.ExistingDealsAdapter;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentExistingDealsBinding;
import com.icashier.app.dialog.EditDealDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.DialogDismissListener;
import com.icashier.app.listener.ExtraItemListener;
import com.icashier.app.model.ExistingDealsModel;
import com.icashier.app.model.GenericResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ExistingDealsFragment extends Fragment {


    FragmentExistingDealsBinding binding;
    HomeActivity context;
    RestClient.ApiRequest apiRequest;
    List<ExistingDealsModel.ResultBean> list=new ArrayList<>();
    ExistingDealsAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_existing_deals, container, false);
        setAdapter();
        callGetDealsApi();

        return binding.getRoot();
    }

    private void setAdapter() {
        adapter=new ExistingDealsAdapter(context, list, new ExtraItemListener() {
            @Override
            public void onDeleteClick(int position) {
                AlertUtil.showAlertWindow(context, getString(R.string.delete_deal_alert), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callDeleteDealApi(position);
                    }
                });
            }

            @Override
            public void onEditClick(int position) {
                new EditDealDialog(context, list.get(position), new DialogDismissListener() {
                    @Override
                    public void onDismiss() {
                        callGetDealsApi();
                    }
                }).show();
            }
        },false,false);
        binding.rvDeals.setLayoutManager(new LinearLayoutManager(context));
        binding.rvDeals.setAdapter(adapter);
    }

    private void callGetDealsApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
           // AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DEALS)
                    .setMethod(RestClient.ApiRequest.METHOD_GET)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                ExistingDealsModel existingDealsModel= new Gson().fromJson(response, ExistingDealsModel.class);
                                if (existingDealsModel != null) {
                                    if(existingDealsModel.getCode()==200){
                                        if(existingDealsModel.getResult().size()>0){
                                            list.clear();
                                            list.addAll(existingDealsModel.getResult());
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

    private void callDeleteDealApi(int position) {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("id",""+list.get(position).getId());

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DELETE_DEAL)
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
