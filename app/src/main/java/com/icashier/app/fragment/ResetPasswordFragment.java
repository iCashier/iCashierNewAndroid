package com.icashier.app.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.FragmentResetPasswordBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.AppValidator;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.GenericResponse;

import java.util.HashMap;


public class ResetPasswordFragment extends Fragment {

    HomeActivity context;
    FragmentResetPasswordBinding binding;
    RestClient.ApiRequest apiRequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context=(HomeActivity)getActivity();
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_reset_password, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setOnClickListeners();
        //to roate views for arabic
        Utilities.rotateViews(binding.flBack);
        return binding.getRoot();
    }

    //================Method to set onClick listeners======================//
    private void setOnClickListeners() {

        binding.btnSubmit.setOnClickListener(V->{
            if(isInputValid()){
                callResetPasswordApi();
            }
        });

        binding.flBack.setOnClickListener(V->{
            context.onBackPressed();
        });

        binding.clParentLayout.setOnClickListener(V->{
            Utilities.hideSoftKeyboard(context);
            context.closeDrawer();
        });
    }


    //===================Mehtod to validate user input==========================//
    private boolean isInputValid()
    {
        if(!binding.etOldPassword.getText().toString().trim().equals("")) {
            if (AppValidator.isValidPassword(binding.clParentLayout, context, binding.etNewPass, getString(R.string.empty_password))) {
                if (!binding.etConfirmPass.getText().toString().trim().equals("")) {
                    if (binding.etConfirmPass.getText().toString().trim().equals(binding.etNewPass.getText().toString())) {
                        if(!binding.etConfirmPass.getText().toString().equals(binding.etOldPassword.getText().toString()))
                        {
                            return true;
                        }else
                        {
                            AlertUtil.toastMsg(context, getString(R.string.same_old_new_pass));

                        }
                    } else {
                        AlertUtil.toastMsg(context, getString(R.string.valid_confirm_pass));
                    }
                } else {
                    AlertUtil.toastMsg(context, getString(R.string.empty_confrim_password));
                    return false;
                }
            }
        }else
        {
            AlertUtil.toastMsg(context,getString(R.string.empty_old_pass));
        }

        return false;
    }
    //==========================Method to call reset password api=====================//
    private void callResetPasswordApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("oldpassword", binding.etOldPassword.getText().toString().trim());
            params.put("newpassword", binding.etNewPass.getText().toString().trim());


            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.RESET_PASSWORD)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setHeader("token", SharedPrefManager.getInstance(context).getString(AppConstant.ACCESS_TOKEN,""))
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (genericResponse.getCode() == 201) {

                                        AlertUtil.toastMsg(context, genericResponse.getMessage());
                                        binding.etNewPass.setText("");
                                        binding.etOldPassword.setText("");
                                        binding.etConfirmPass.setText("");
                                        context.onBackPressed();

                                    }else {

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
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
    }
}
