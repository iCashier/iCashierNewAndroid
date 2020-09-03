package com.icashier.app.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentDineinCodeBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.AddTableDialogListener;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.TableListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DineinCodeFragment extends Fragment implements AddTableDialogListener {

    HomeActivity context;
    FragmentDineinCodeBinding binding;
    RestClient.ApiRequest apiRequest;
    List<String> tableNameList=new ArrayList<>();
    List<TableListResponse.ResultBean> tableList=new ArrayList<>();
    String paymentMode="",tableId="";
    boolean isCreditCard,isCash,isPaypal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_dinein_code, container, false);
        setOnClickListeners();
        return binding.getRoot();
    }

    //======================Method to set onClick listeners================//
    private void setOnClickListeners() {

        binding.clParentLayout.setOnClickListener(V->{
            context.closeDrawer();
            Utilities.hideSoftKeyboard(context);
        });

        binding.llCash.setOnClickListener(V->{
            setPaymentSelector(binding.imgCash);
        });

        binding.llCreditCard.setOnClickListener(V->{
            setPaymentSelector(binding.imgCreditCard);
        });

        binding.llPaypal.setOnClickListener(V->{
            setPaymentSelector(binding.imgPaypal);
        });



        binding.flSave.setOnClickListener(V->{
            if(isValidInput()){
                callGenerateCodeApi();
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private boolean isValidInput()
    {
            if(!binding.etTable.getText().toString().trim().equals("")){
                if(isPaypal||isCash||isCreditCard) {
                    return true;
                }else{
                    AlertUtil.toastMsg(context,getString(R.string.empty_payment_mode));

                }
            }else{
                AlertUtil.toastMsg(context,getString(R.string.empty_table));
            }

        return false;
    }
    //=================MEhtod to select payment method=============//
    private void setPaymentSelector(ImageView img)
    {
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


    //============Method to call get table list api===========//
    private void callGetTableListApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_TABLE_LIST)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    TableListResponse tableListResponse= new Gson().fromJson(response, TableListResponse.class);
                                    if (tableListResponse != null) {
                                        if (tableListResponse.getCode()==200 ) {
                                            tableList.clear();
                                            tableList.addAll(tableListResponse.getResult());

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

    //================MEthod to show drop down===================//
    private void showDropDown() {
        tableNameList.clear();
        for(int i=0;i<tableList.size();i++){
            tableNameList.add(tableList.get(i).getName());
        }
        ArrayAdapter listPopupAdapter=new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,tableNameList);
        ListPopupWindow popupWindow=new ListPopupWindow(context);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int index, long id) {
                binding.etTable.setText(tableNameList.get(index));
                tableId=""+tableList.get(index).getId();
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentWidth(binding.etTable.getWidth());
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setAnchorView(binding.etTable);
        popupWindow.setAdapter(listPopupAdapter);
        popupWindow.show();

    }

    @Override
    public void onDialogDismissed(List<TableListResponse.ResultBean> list) {
        tableList=list;
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
            params.put("type","dine_in");
            params.put("table_id",binding.etTable.getText().toString().trim());
            params.put("payment",paymentMode);
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
                                            isCash=false;
                                            isCreditCard=false;
                                            isPaypal=false;
                                            paymentMode="";
                                            tableId="";
                                            binding.etTable.setText("");
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
}
