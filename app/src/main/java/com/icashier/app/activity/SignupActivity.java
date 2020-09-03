package com.icashier.app.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.countryPicker.CountryPickerActivity;
import com.icashier.app.databinding.ActivitySignupBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.AppValidator;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.SigninResponse;

import java.util.HashMap;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    SignupActivity context;
    RestClient.ApiRequest apiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        binding= DataBindingUtil.setContentView(context,R.layout.activity_signup);

        //to rotate views for arabic
        Utilities.rotateViews(binding.flBack);
        setOnClickListener();
    }

    //==================Mehtod to set onClick listener==================//
    private void setOnClickListener() {
        binding.flSignup.setOnClickListener(V->{
            if(isInputValid())
            {
                callSignUpApi();
            }
        });

        binding.tvCode.setOnClickListener(V->{
            Intent intent=new Intent(context, CountryPickerActivity.class);
            startActivityForResult(intent,5);
        });

        binding.flBack.setOnClickListener(V->{finish();});
        binding.clParentLayout.setOnClickListener(V->{
            Utilities.hideSoftKeyboard(context);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==5){
            String code=data.getStringExtra("COUNTRY_CODE");
            binding.tvCode.setText(code);
            binding.imgFlag.setImageResource(CountryPickerActivity.getFlagByCountryCode(code));
        }
    }



    //====================Method to validate user input================//
    private boolean isInputValid()
    {
        if(!binding.etName.getText().toString().trim().equals(""))
        {
            if(AppValidator.isValidEmail(binding.clParentLayout,context,binding.etEmail,getString(R.string.empty_email)))
            {
                if(AppValidator.isValidPassword(binding.clParentLayout,context,binding.etPassword,getString(R.string.empty_password)))
                {
                    if (!binding.etConfirmPass.getText().toString().trim().equals(""))
                    {
                        if (binding.etConfirmPass.getText().toString().trim().equals(binding.etPassword.getText().toString()))
                        {
                            if(AppValidator.isValidMobile(binding.clParentLayout,context,binding.etMobile))
                            {
                                return true;
                            }
                        }
                        else
                        {
                            AlertUtil.toastMsg(context,getString(R.string.valid_confirm_pass));
                        }
                    }
                    else
                    {
                        AlertUtil.toastMsg(context,getString(R.string.empty_confrim_password));
                        return false;
                    }
                }
            }

        }else
        {
            AlertUtil.toastMsg(context,getString(R.string.empty_name));
            return false;
        }
        return false;
    }
    //==========================Method to call signup user api=====================//
    private void callSignUpApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("email", binding.etEmail.getText().toString().trim());
            params.put("password", binding.etPassword.getText().toString().trim());
            params.put("mobile", binding.etMobile.getText().toString().trim());
            params.put("name", binding.etName.getText().toString().trim());
            params.put("country_code", binding.tvCode.getText().toString().trim());
            params.put("device_type","android");
            params.put("device_id",SharedPrefManager.getInstance(context).getString(AppConstant.KEY_DEVICE_TOKEN,""));
            params.put("lang",Utilities.isArabic()?"1":"0");  //1 for arabic and 0 for english



            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.SIGN_UP)
                    .setMethod(RestClient.ApiRequest.METHOD_MULTIPART)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                           SigninResponse signupResponse= new Gson().fromJson(response, SigninResponse.class);
                            if (signupResponse != null) {
                                AlertUtil.hideProgressDialog();
                                if (signupResponse.getCode()==201 ) {

                                    AlertUtil.toastMsg(context, signupResponse.getMessage());
                                    SharedPrefManager.getInstance(context).saveString(AppConstant.ACCESS_TOKEN,signupResponse.getResult().getToken());
                                    Intent intent =new Intent(context,OtpActivity.class);
                                    intent.putExtra(AppConstant.COUNTRY_CODE,signupResponse.getResult().getCountry_code());
                                    intent.putExtra(AppConstant.PHONE_NO,signupResponse.getResult().getMobile());
                                    startActivity(intent);

                                } else {

                                    AlertUtil.toastMsg(context, signupResponse.getMessage());
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
