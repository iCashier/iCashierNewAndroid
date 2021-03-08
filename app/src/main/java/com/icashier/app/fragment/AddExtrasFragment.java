package com.icashier.app.fragment;


import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.adapter.ExtrasListAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentAddExtrasBinding;
import com.icashier.app.dialog.EditExtrasDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.DecimalDigitsInputFilter;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.CashierSignoutListener;
import com.icashier.app.listener.EditExtrasDialogListener;
import com.icashier.app.listener.ExtraItemListener;
import com.icashier.app.model.ExtrasListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddExtrasFragment extends Fragment {


    HomeActivity context;
    FragmentAddExtrasBinding binding;
    String type="radiobutton";
    RestClient.ApiRequest apiRequest;
    List<ExtrasListResponse.ResultBean> extrasList=new ArrayList<>();
    ExtrasListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context=(HomeActivity)getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_add_extras, container, false);
        setOnClickListener();
        binding.etPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
        setAdapter();
        return binding.getRoot();
    }

    //====================Method to set extras list adapter=============//
    private void setAdapter() {

        adapter=new ExtrasListAdapter(context, extrasList, new ExtraItemListener() {
            @Override
            public void onDeleteClick(int position) {
                AlertUtil.showAlertWindow(context, getString(R.string.delete_extra_alert), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callDeleteExtrasApi(extrasList.get(position).getId());
                    }
                });
            }

            @Override
            public void onEditClick(int position) {
                new EditExtrasDialog(context, extrasList.get(position), new EditExtrasDialogListener() {
                    @Override
                    public void onUpdateClick(ExtrasListResponse.ResultBean updatedExtra) {
                        callEditExtrasApi(updatedExtra);
                    }
                }).show();
            }
        });
        binding.rvExtras.setLayoutManager(new LinearLayoutManager(context));
        binding.rvExtras.setAdapter(adapter);
        callGetExtrasListApi();

    }

    //=======================MEthod to set onClick listener====================//
    private void setOnClickListener() {
        binding.llParentLayout.setOnClickListener(V->{
            context.closeDrawer();
            Utilities.hideSoftKeyboard(context);
        });

        binding.tvType.setOnClickListener(V->{
            showDropDown();
        });

        binding.flSave.setOnClickListener(V->{
            if(isInputValid()){
                callAddExtrasApi();
            }
        });

    }
    //==========================Method to call get extras  api=====================//
    private void callGetExtrasListApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            //AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();

            apiRequest = new RestClient.ApiRequest(context);


            HashMap<String,String> headers=new HashMap<>();
            headers.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            headers.put("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken",SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,""));

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_EXTRAS_LIST)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    ExtrasListResponse extrasListResponse= new Gson().fromJson(response, ExtrasListResponse.class);
                                    if (extrasListResponse != null) {
                                        AlertUtil.hideProgressDialog();
                                        if (extrasListResponse.getCode()==200 ) {

                                            extrasList.clear();
                                            extrasList.addAll(extrasListResponse.getResult());
                                            if(extrasList.size()>0){
                                                binding.tvExistingExtras.setVisibility(View.VISIBLE);
                                            }else{
                                                binding.tvExistingExtras.setVisibility(View.GONE);
                                            }
                                            adapter.notifyDataSetChanged();
                                        } else if(extrasListResponse.getCode()==301)
                                        {
                                            Utilities.signoutCashier(context, new CashierSignoutListener() {
                                                @Override
                                                public void onCashierExpire() {
                                                    callGetExtrasListApi();
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
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
    }

    //==========================Method to call add extras  api=====================//
    private void callAddExtrasApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("title",binding.etTittle.getText().toString().trim());
            params.put("titleAr",binding.arabicName.getText().toString().trim());
            params.put("type",type);
            params.put("price",binding.etPrice.getText().toString().trim());


            HashMap<String,String> headers=new HashMap<>();
            headers.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            headers.put("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken",SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,""));

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.ADD_EXTRAS)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                ExtrasListResponse extrasListResponse= new Gson().fromJson(response, ExtrasListResponse.class);
                                if (extrasListResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (extrasListResponse.getCode()==200 ) {

                                        binding.etTittle.setText("");
                                        binding.arabicName.setText("");
                                        binding.etPrice.setText("");
                                        binding.etTittle.requestFocus();
                                        extrasList.clear();
                                        extrasList.addAll(extrasListResponse.getResult());
                                        if(extrasList.size()>0){
                                            binding.tvExistingExtras.setVisibility(View.VISIBLE);
                                        }else{
                                            binding.tvExistingExtras.setVisibility(View.GONE);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }else if(extrasListResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callAddExtrasApi();
                                            }
                                        });
                                    }else{
                                        AlertUtil.showToastShort(context,extrasListResponse.getMessage());
                                    }
                                } else {
                                    AlertUtil.hideProgressDialog();
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

    //==========================Method to call delete extras  api=====================//
    private void callDeleteExtrasApi(int id) {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("id",""+id);


            HashMap<String,String> headers=new HashMap<>();
            headers.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            headers.put("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken",SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,""));

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.DELETE_EXTRAS)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                ExtrasListResponse extrasListResponse= new Gson().fromJson(response, ExtrasListResponse.class);
                                if (extrasListResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (extrasListResponse.getCode()==200 ) {

                                        extrasList.clear();
                                        extrasList.addAll(extrasListResponse.getResult());
                                        if(extrasList.size()>0){
                                            binding.tvExistingExtras.setVisibility(View.VISIBLE);
                                        }else{
                                            binding.tvExistingExtras.setVisibility(View.GONE);
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else if(extrasListResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callGetExtrasListApi();
                                            }
                                        });
                                    }
                                } else {
                                    AlertUtil.hideProgressDialog();
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

    //==========================Method to call edit_yellow extras  api=====================//
    private void callEditExtrasApi(ExtrasListResponse.ResultBean extraData) {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideSoftKeyboard(context);
            AlertUtil.showProgressDialog(context);
            HashMap<String, String> params = new HashMap<>();
            params.put("title",extraData.getTitle());
            params.put("titleAr",extraData.getTitleAr());
            params.put("type",extraData.getType());
            params.put("price",extraData.getPrice());
            params.put("id",""+extraData.getId());



            HashMap<String,String> headers=new HashMap<>();
            headers.put("token",SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));
            headers.put("type",SharedPrefManager.getInstance(context).getString(AppConstant.USER_TYPE,AppConstant.TYPE_MERCHANT));
            headers.put("cashiertoken",SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,""));

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.EDIT_EXTRAS)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeaders(headers)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                ExtrasListResponse extrasListResponse= new Gson().fromJson(response, ExtrasListResponse.class);
                                if (extrasListResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (extrasListResponse.getCode()==200 ) {

                                        extrasList.clear();
                                        extrasList.addAll(extrasListResponse.getResult());
                                        if(extrasList.size()>0){
                                            binding.tvExistingExtras.setVisibility(View.VISIBLE);
                                        }else{
                                            binding.tvExistingExtras.setVisibility(View.GONE);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }else if(extrasListResponse.getCode()==301)
                                    {
                                        Utilities.signoutCashier(context, new CashierSignoutListener() {
                                            @Override
                                            public void onCashierExpire() {
                                                callEditExtrasApi(extraData);
                                            }
                                        });
                                    }
                                } else {
                                    AlertUtil.hideProgressDialog();
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

    //================MEthod to show drop down===================//
    private void showDropDown() {
        String list[]=new String[]{context.getString(R.string.radio_button),context.getString(R.string.check_box)};
        ArrayAdapter listPopupAdapter=new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,list);
        ListPopupWindow popupWindow=new ListPopupWindow(context);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int index, long id) {

                binding.tvType.setText(list[index]);
                if(index==0){
                    type="radiobutton";
                }else {
                    type="checkbox";
                }
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentWidth(binding.tvType.getWidth());
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setAnchorView(binding.tvType);
        popupWindow.setAdapter(listPopupAdapter);
        popupWindow.show();

    }

    //===================Mehtod to validate user input==============//
    private boolean isInputValid()
    {
        if(!binding.etTittle.getText().toString().trim().equals(""))
        {
            if(!binding.etPrice.getText().toString().trim().equals(""))
            {
                return true;
            }
            else
            {
                AlertUtil.toastMsg(context,context.getString(R.string.empty_price));

            }
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.empty_title));
        }
        return false;
    }


}
