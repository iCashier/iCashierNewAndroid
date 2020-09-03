package com.icashier.app.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.ItemsInInvoiceAdapter;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentNewInvoiceBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.AppValidator;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.OrderListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NewInvoiceFragment extends Fragment {

    HomeActivity context;
    FragmentNewInvoiceBinding binding;
    RestClient.ApiRequest apiRequest;
    List<String> list=new ArrayList<>();
    OrderListResponse.ResultBean invoiceData;
    List<OrderListResponse.ResultBean> invoiceList=new ArrayList<>();
    List<OrderListResponse.ResultBean.ItemsBean> itemsList=new ArrayList<>();
    ItemsInInvoiceAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_new_invoice, container, false);
        setOnClickListener();
        callGetOrdersApi();
        return binding.getRoot();
    }

    //=========================Method to set onClick listener==========//
    private void setOnClickListener() {

        binding.tvInvoiceId.setOnClickListener(V->{
            showInvoiceDropDown();
        });

        binding.imgSend.setOnClickListener(V->{
            if(isInputValid()){
                callSaveInvoiceApi();
            }
        });
    }

    //================mehtod to set data========//
    private void setData() {
        binding.etName.setText(invoiceData.getCustomerName());
        binding.tvTotal.setText("SR "+invoiceData.getTotal());

        itemsList.clear();
        itemsList.addAll(invoiceData.getItems());
        itemsList.addAll(invoiceData.getMeal());
        itemsList.addAll(invoiceData.getDeals());

        adapter=new ItemsInInvoiceAdapter(context,itemsList);
        binding.rvItems.setAdapter(adapter);
        binding.rvItems.setLayoutManager(new LinearLayoutManager(context));
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
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                OrderListResponse orderListResponse= new Gson().fromJson(response, OrderListResponse.class);
                                if (orderListResponse != null) {
                                    if(orderListResponse.getCode()==200){
                                        if(orderListResponse.getResult().size()>0){
                                            invoiceList.addAll(orderListResponse.getResult());
                                            for(int i=0;i<invoiceList.size();i++){
                                                list.add("#"+invoiceList.get(i).getSequence_no());
                                            }
                                        }
                                    }else if(orderListResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callGetOrdersApi();
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

    //======================Mehtod to show primary category list popup=======================//
    private void showInvoiceDropDown() {

        ArrayAdapter listPopupAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, list);

        ListPopupWindow popupWindow = new ListPopupWindow(context);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int index, long id) {

                binding.tvInvoiceId.setText(list.get(index));
                invoiceData=invoiceList.get(index);
                setData();
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentWidth(binding.tvInvoiceId.getWidth());
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setAnchorView(binding.tvInvoiceId);
        popupWindow.setAdapter(listPopupAdapter);
        popupWindow.show();

    }

    private boolean isInputValid(){

        if(!binding.tvInvoiceId.getText().toString().trim().equals("")) {
            if (!binding.etInvoiceTitle.getText().toString().trim().equals("")) {
                if (!binding.etName.getText().toString().trim().equals("")) {

                    if(AppValidator.isValidEmail(binding.clParentLayout,context,binding.tvEmail,getString(R.string.empty_email))) {
                        if (AppValidator.isValidMobile(binding.clParentLayout, context, binding.etPhone)) {
                            return true;
                        }
                    }
                } else {
                    AlertUtil.toastMsg(context, getString(R.string.empty_customer));
                }
            } else {
                AlertUtil.toastMsg(context, getString(R.string.empty_invoice_title));
            }
        }else{
            AlertUtil.toastMsg(context, getString(R.string.empty_invoice_id));

        }
        return false;
    }

    //==========================Method to get order list api=====================//
    private void callSaveInvoiceApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();

            params.put("invoiceId",""+binding.tvInvoiceId.getText().toString().trim());
            params.put("title",binding.etInvoiceTitle.getText().toString().trim());
            params.put("customerName",binding.etName.getText().toString().trim());
            params.put("notes",binding.etNotes.getText().toString().trim());
            params.put("email",binding.tvEmail.getText().toString().trim());
            params.put("cc",binding.etCC.getText().toString().trim());
            params.put("mobile",binding.etPhone.getText().toString().trim());
            params.put("recept",invoiceData.getPdf());
            params.put("type","send");


            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.ADD_INVOICE)
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
                                        clearFields();
                                        AlertUtil.toastMsg(context, genericResponse.getMessage());

                                    }else if(genericResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {

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

    private void clearFields() {

        binding.tvInvoiceId.setText("");
        invoiceData=null;
        binding.etInvoiceTitle.setText("");
        binding.etName.setText("");
        binding.etPhone.setText("");
        binding.tvEmail.setText("");
        binding.etCC.setText("");
        binding.tvTotal.setText("");
        binding.etNotes.setText("");
        itemsList.clear();
        adapter.notifyDataSetChanged();

    }

}
