package com.icashier.app.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.activity.LoginActivity;
import com.icashier.app.adapter.CashierListAdapter;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogCasheirLoginBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.listener.DialogDismissListener;
import com.icashier.app.listener.SelectCashierListener;
import com.icashier.app.model.CashierLoginResponse;
import com.icashier.app.model.GenericResponse;
import com.icashier.app.model.GetCashiersResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CashierLoginDialog  extends Dialog {

    HomeActivity context;
    private LayoutInflater inflater;
    DialogCasheirLoginBinding binding;

    RestClient.ApiRequest apiRequest;
    List<GetCashiersResponse.CashiersBean> list=new ArrayList<>();
    CashierListAdapter adapter;
    DialogDismissListener listener;

    public CashierLoginDialog(HomeActivity context,DialogDismissListener listener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_casheir_login, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);
        setAdapter();

        if(!SharedPrefManager.getInstance(context).getString(AppConstant.CASHIER_TOKEN,"").equals("")){
            binding.rlFields.setVisibility(View.GONE);
            binding.btnSubmit.setVisibility(View.GONE);
        }
        binding.btnCancel.setOnClickListener(V->{
            dismiss();
            listener.onDismiss();
        });

        binding.btnSubmit.setOnClickListener(V->{
            if(isValidInput()){
                callLoginCasheirApi();
            }
        });

    }

    //===============Method to set Adapter============//
    private void setAdapter() {
        adapter=new CashierListAdapter(context, list, new SelectCashierListener() {
            @Override
            public void onCasheirSelected(String name) {
                binding.etName.setText(name);
                binding.rlFields.setVisibility(View.VISIBLE);
                binding.btnSubmit.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLogoutClick(int position) {
                AlertUtil.showAlertWindow(context, context.getString(R.string.signout_confirmation), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPrefManager.getInstance(context).removeData(AppConstant.CASHIER_TOKEN);
                        SharedPrefManager.getInstance(context).removeData(AppConstant.CASHIER_NAME);
                        SharedPrefManager.getInstance(context).removeData(AppConstant.USER_TYPE);
                        list.get(position).setSelected(false);
                        adapter.notifyDataSetChanged();
                        binding.etName.setText("");
                        binding.rlFields.setVisibility(View.VISIBLE);
                        binding.btnSubmit.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        binding.rvCashiers.setAdapter(adapter);
        binding.rvCashiers.setLayoutManager(new GridLayoutManager(context,3));
        callGetCashiersApi();

    }

    private boolean isValidInput() {
        Utilities.hideKeyboardOnPopup(context,binding.etName);
        if (binding.etName.getText().toString().trim().length() < 1) {
            AlertUtil.toastMsg(context,context.getString(R.string.empty_name));
            return false;
        }
        if (binding.etPassword.getText().toString().trim().length() < 1) {
            AlertUtil.toastMsg(context, context.getString(R.string.empty_password));
            return false;
        }else return true;

    }
    //==========================Method to call get cashiers list api=====================//
    private void callGetCashiersApi() {
        if(Utilities.isNetworkAvailable(context)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();
            params.put("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""));

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.GET_CASHIER_LIST)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    GetCashiersResponse getCashiersResponse= new Gson().fromJson(response, GetCashiersResponse.class);
                                    if (getCashiersResponse != null) {
                                        if (getCashiersResponse.getCode()==200 ) {
                                            list.addAll(getCashiersResponse.getCashiers());
                                            adapter.notifyDataSetChanged();
                                        }

                                    } else {
                                        binding.progressBar.setVisibility(View.VISIBLE);
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            binding.progressBar.setVisibility(View.VISIBLE);
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.network_error));
        }
    }

    //==========================Method to call login cashier  api=====================//
    private void callLoginCasheirApi() {
        if(Utilities.isNetworkAvailable(context)) {
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();
            params.put("name", binding.etName.getText().toString().trim());
            params.put("passcode", binding.etPassword.getText().toString().trim());


            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.LOGIN_CASHIER)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (Utilities.isValidJson(response)) {
                                if (Utilities.isValidJson(response)) {
                                    CashierLoginResponse cashierLoginResponse= new Gson().fromJson(response, CashierLoginResponse.class);
                                    if (cashierLoginResponse != null) {
                                        if (cashierLoginResponse.getCode()==200) {
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.CASHIER_TOKEN,cashierLoginResponse.getResult().getToken());
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.USER_TYPE,AppConstant.TYPE_ICASHEIR);
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.CASHIER_NAME,cashierLoginResponse.getResult().getName());
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.CASHIER_IMAGE,cashierLoginResponse.getResult().getImage());
                                            AlertUtil.showToastLong(context,cashierLoginResponse.getMessage());
                                            listener.onDismiss();
                                            dismiss();
                                        }else{
                                            AlertUtil.toastMsg(context, cashierLoginResponse.getMessage());
                                        }

                                    } else {
                                        binding.progressBar.setVisibility(View.GONE);
                                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                                    }
                                }
                            }
                        }
                    })
                    .setErrorListener(new RestClient.ErrorListener() {
                        @Override
                        public void onError(String tag, String errorMsg) {
                            binding.progressBar.setVisibility(View.VISIBLE);
                            AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                        }
                    })
                    .execute();
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.network_error));
        }
    }


    //==========================Method to call logout api=====================//
    private void callLogoutApi(int position) {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("flag","0");

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.SIGNOUT)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                GenericResponse genericResponse= new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (genericResponse.getCode()==200 ) {

                                        SharedPrefManager.getInstance(context).removeData(AppConstant.CASHIER_TOKEN);
                                        SharedPrefManager.getInstance(context).removeData(AppConstant.CASHIER_NAME);
                                        SharedPrefManager.getInstance(context).removeData(AppConstant.USER_TYPE);
                                        list.get(position).setSelected(false);
                                        adapter.notifyDataSetChanged();
                                        binding.etName.setText("");
                                        binding.rlFields.setVisibility(View.VISIBLE);
                                        binding.btnSubmit.setVisibility(View.VISIBLE);
                                        AlertUtil.toastMsg(context, genericResponse.getMessage());


                                    } else {

                                        AlertUtil.toastMsg(context, genericResponse.getMessage());
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
            AlertUtil.toastMsg(context,context.getString(R.string.network_error));
        }
    }
}