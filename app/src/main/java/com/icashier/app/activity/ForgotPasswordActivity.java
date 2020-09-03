package com.icashier.app.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.ActivityForgotPasswordBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.AppValidator;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.GenericResponse;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    ForgotPasswordActivity context;
    RestClient.ApiRequest apiRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        binding= DataBindingUtil.setContentView(context,R.layout.activity_forgot_password);

        setOnClickListener();

        //to rotate views for arabic
        Utilities.rotateViews(binding.flBack);
    }


    //=================Method to set onClick Listeners=================//
    private void setOnClickListener() {
        binding.btnSubmit.setOnClickListener(V->{

            if(isInputValid())
            {
                callForgotPasswordApi();
            }
        });

        binding.flBack.setOnClickListener(V->{finish();});
    }

    //==================Method to validate user input==============//
    private boolean isInputValid()
    {
        if(AppValidator.isValidEmail(binding.clParentLayout,context,binding.etEmail,getString(R.string.valid_email)))
        {
            return true;
        }
        return false;
    }


    //==========================Method to call forgot password api=====================//
    private void callForgotPasswordApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("email", binding.etEmail.getText().toString().trim());
            params.put("lang",Utilities.isArabic()?"1":"0");  //1 for arabic and 0 for english

            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.FORGOT_PASSWORD)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                GenericResponse genericResponse = new Gson().fromJson(response, GenericResponse.class);
                                if (genericResponse != null) {
                                    AlertUtil.hideProgressDialog();
                                    if (genericResponse.getCode() == 201) {

                                        AlertUtil.toastMsgLong(context, genericResponse.getMessage());
                                    } else {

                                        AlertUtil.toastMsg(context,  genericResponse.getMessage());
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
