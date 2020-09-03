package com.icashier.app.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentTakeOutCodeBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.model.ExistingItemList;
import com.icashier.app.model.GenericResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TakeOutCodeFragment extends Fragment {

    HomeActivity context;
    FragmentTakeOutCodeBinding binding;
    RestClient.ApiRequest apiRequest;
    List<ExistingItemList.ResultBean> itemList=new ArrayList<>();
    List<String> itemNamesList=new ArrayList<>();
    ExistingItemList.ResultBean item;
    String paymentMode="";
    boolean isCreditCard,isCash,isPaypal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_take_out_code, container, false);
        setOnClickListeners();

        //callGetItemListApi();
        return binding.getRoot();
    }

    //======================Method to set onClick listeners================//
    private void setOnClickListeners() {

        binding.clParentLayout.setOnClickListener(V -> {
            context.closeDrawer();
            Utilities.hideSoftKeyboard(context);
        });

        binding.llCash.setOnClickListener(V -> {
            setPaymentSelector(binding.imgCash);
        });

        binding.llCreditCard.setOnClickListener(V -> {
            setPaymentSelector(binding.imgCreditCard);
        });

        binding.llPaypal.setOnClickListener(V -> {
            setPaymentSelector(binding.imgPaypal);
        });



        binding.flSave.setOnClickListener(V->{
            if(isValidInput()){
                callGenerateCodeApi();
            }
        });
    }

    //============Method to call get table list api===========//
    private void callGenerateCodeApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);
            paymentMode="";
            if(isCreditCard){
                if(paymentMode.equals("")){
                    paymentMode=AppConstant.CREDIT_CARD;
                }else{
                    paymentMode=paymentMode+","+AppConstant.CREDIT_CARD;
                }
            }
            if(isCash){
                if(paymentMode.equals("")){
                    paymentMode=AppConstant.CASH;
                }else{
                    paymentMode=paymentMode+","+AppConstant.CASH;
                }
            }

            if(isPaypal){
                if(paymentMode.equals("")){
                    paymentMode=AppConstant.PAYPAL;
                }else{
                    paymentMode=paymentMode+","+AppConstant.PAYPAL;
                }
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("type",AppConstant.TYPE_PICKUP);
            params.put("payment",paymentMode);
            params.put("name",binding.etItem.getText().toString().trim());

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.CREATE_QR)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    GenericResponse genericResponse= new Gson().fromJson(response, GenericResponse.class);
                                    if (genericResponse != null) {
                                        if (genericResponse.getCode()==200 ) {

                                            binding.imgPaypal.setSelected(false);
                                            binding.imgCash.setSelected(false);
                                            binding.imgCreditCard.setSelected(false);
                                            binding.etItem.setText("");
                                            paymentMode="";
                                            item=null;

                                            AlertUtil.toastMsg(context,genericResponse.getMessage());

                                        } else if(genericResponse.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callGenerateCodeApi();
                                                }
                                            });
                                        }else{
                                            AlertUtil.toastMsg(context, genericResponse.getMessage());

                                        }
                                    } else {
                                        AlertUtil.hideProgressDialog();
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
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }

    }
    //==========Mehtod to validate user input===========//
    private boolean isValidInput() {
        if(!binding.etItem.getText().toString().trim().equals("")){
            if(isCash||isCreditCard||isPaypal){
                return true;
            }else{
                AlertUtil.toastMsg(context,getString(R.string.empty_payment_mode));
            }
        }else{
            AlertUtil.toastMsg(context,getString(R.string.empty_item));
        }
        return false;
    }
    //==========================Method to call get existing items  api=====================//
    private void callGetItemListApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));


            HashMap<String,String> headers=new HashMap<>();
            headers.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            headers.put("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken",SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,""));
            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_ITEM_LIST)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    ExistingItemList existingItemList= new Gson().fromJson(response, ExistingItemList.class);
                                    if (existingItemList != null) {
                                        AlertUtil.hideProgressDialog();
                                        if (existingItemList.getCode()==200 ) {
                                            itemList.addAll(existingItemList.getResult());
                                            for(int i=0;i<itemList.size();i++){
                                                itemNamesList.add(itemList.get(i).getName());
                                            }
                                        } else if(existingItemList.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callGetItemListApi();
                                                }
                                            });
                                        }

                                    } else {
                                        AlertUtil.hideProgressDialog();
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

    //=================MEhtod to select payment method=============//
    private void setPaymentSelector(ImageView img) {
        if(img==binding.imgPaypal)
        {
            if(!isPaypal){
                binding.imgPaypal.setSelected(true);
                isPaypal=true;
            }else{
                binding.imgPaypal.setSelected(false);
                isPaypal=false;
            }

        }
        else if(img==binding.imgCreditCard)
        {
            if(!isCreditCard){
                binding.imgCreditCard.setSelected(true);
                isCreditCard=true;
            }else{
                binding.imgCreditCard.setSelected(false);
                isCreditCard=false;
            }
        }
        else if(img==binding.imgCash)
        {
            if(!isCash){
                binding.imgCash.setSelected(true);
                isCash=true;
            }else{
                binding.imgCash.setSelected(false);
                isCash=false;
            }
        }
    }


}
