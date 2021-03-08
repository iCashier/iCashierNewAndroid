package com.icashier.app.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.location.Location;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.ActivityLoginBinding;
import com.icashier.app.dialog.CreateOutletDialog;
import com.icashier.app.dialog.SelectOutletDialog;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.AppValidator;
import com.icashier.app.helper.LocationActivity;
import com.icashier.app.helper.PermissionsUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.SigninResponse;

import java.util.HashMap;
import java.util.Locale;

public class LoginActivity extends LocationActivity {

    ActivityLoginBinding binding;
    LoginActivity context;
    RestClient.ApiRequest apiRequest;
    double lat,lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        binding= DataBindingUtil.setContentView(context,R.layout.activity_login);

        //to underline text
        binding.tvSignup.setPaintFlags(binding.tvSignup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //to set animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.txt_bottom_up);
        binding.centerLayout.startAnimation(animation);
        requestLocation(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat=location.getLatitude();
                lng=location.getLongitude();
            }
        });

        setOnClickListeners();
    }

    //===========================Method to set onclickListeners==================//
    private void setOnClickListeners() {

        binding.tvSignup.setOnClickListener(V->{
            Utilities.goToActivity(context,SignupActivity.class);
        });

        binding.btnLogin.setOnClickListener(V->{
            if(isInputValid()){
                if(lat!=0) {
                    callSignInApi();
                }else{
                    requestLocation(new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            lat=location.getLatitude();
                            lng=location.getLongitude();
                            callSignInApi();
                        }
                    });
                }
            }
        });

        binding.clParentLayout.setOnClickListener(V->{Utilities.hideSoftKeyboard(context);});
        binding.tvForgotPass.setOnClickListener(V->{
            Utilities.goToActivity(context,ForgotPasswordActivity.class);
        });
    }


    //======================Method to validate user input====================//
    private boolean isInputValid()
    {
        if(AppValidator.isValidEmail(binding.clParentLayout,context,binding.etEmail,getString(R.string.empty_email)))
        {
            if(AppValidator.isValidPassword(binding.clParentLayout,context,binding.etPassword,getString(R.string.empty_password)))
            {
                return true;
            }
        }
        return false;
    }

    //==========================Method to call signin user api=====================//
    private void callSignInApi() {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("email", binding.etEmail.getText().toString().trim());
            params.put("password", binding.etPassword.getText().toString().trim());
            params.put("device_type","android");
            params.put("device_id",SharedPrefManager.getInstance(context).getString(AppConstant.KEY_DEVICE_TOKEN,""));
            params.put("lat",""+lat);
            params.put("lng",""+lng);
            params.put("lang",Utilities.isArabic()?"1":"0");  //1 for arabic and 0 for english


            apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.SIGN_IN)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener(new RestClient.ResponseListener() {
                        @Override
                        public void onResponse(String tag, String response) {
                            AlertUtil.hideProgressDialog();
                            if (Utilities.isValidJson(response)) {
                                SigninResponse loginResponse = new Gson().fromJson(response, SigninResponse.class);
                                if (loginResponse != null) {
                                    AlertUtil.hideProgressDialog();

                                    if (loginResponse.getCode() == 201) {
                                        if(!loginResponse.getResult().getPlanid().equals("")) {
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.USER_INFO, new Gson().toJson(loginResponse.getResult()));
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.KEY_LOGIN_USER_ID, loginResponse.getResult().getUid());
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.ACCESS_TOKEN, loginResponse.getResult().getToken());
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.USER_TYPE, AppConstant.TYPE_MERCHANT);
                                            SharedPrefManager.getInstance(context).saveInt(AppConstant.IS_PARENT,loginResponse.getResult().getIsParent());

                                            //Clears previous activities
                                            Utilities.clearAllgoToActivity(context, HomeActivity.class);
                                        }else{
                                            /*SharedPrefManager.getInstance(context).saveString(AppConstant.ACCESS_TOKEN, loginResponse.getResult().getToken());
                                            Intent intent=new Intent(context,CompletePlansActivity.class);
                                            intent.putExtra(AppConstant.SIGNIN_RESPONSE,loginResponse.getResult());
                                            startActivity(intent);*/
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.USER_INFO, new Gson().toJson(loginResponse.getResult()));
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.KEY_LOGIN_USER_ID, loginResponse.getResult().getUid());
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.ACCESS_TOKEN, loginResponse.getResult().getToken());
                                            SharedPrefManager.getInstance(context).saveString(AppConstant.USER_TYPE, AppConstant.TYPE_MERCHANT);
                                            SharedPrefManager.getInstance(context).saveInt(AppConstant.IS_PARENT,loginResponse.getResult().getIsParent());

                                            //Clears previous activities
                                            Utilities.clearAllgoToActivity(context, HomeActivity.class);

                                        }
                                    } else if(loginResponse.getCode()==202){
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.USER_INFO, new Gson().toJson(loginResponse.getResult()));
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.ACCESS_TOKEN, loginResponse.getResult().getToken());
                                        AlertUtil.toastMsg(context,  loginResponse.getMessage());
                                        Intent intent =new Intent(context,OtpActivity.class);
                                        intent.putExtra(AppConstant.COUNTRY_CODE,loginResponse.getResult().getCountry_code());
                                        intent.putExtra(AppConstant.PHONE_NO,loginResponse.getResult().getMobile());
                                        startActivity(intent);
                                    }else if(loginResponse.getCode()==203){
                                           /* Intent intent = new Intent(context, CreateOutletDialog.class);
                                            intent.putExtra(AppConstant.LATITUDE, lat);
                                            intent.putExtra(AppConstant.LONGITUDE, lng);
                                            intent.putExtra(AppConstant.OUTLET,new Gson().toJson(loginResponse.getList()));
                                            intent.putExtra(AppConstant.EMAIL, binding.etEmail.getText().toString());
                                            intent.putExtra(AppConstant.PASSWORD, binding.etPassword.getText().toString());
                                            startActivity(intent);*/

                                        SharedPrefManager.getInstance(context).saveString(AppConstant.USER_INFO, new Gson().toJson(loginResponse.getResult()));
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.KEY_LOGIN_USER_ID, loginResponse.getResult().getUid());
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.ACCESS_TOKEN, loginResponse.getResult().getToken());
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.USER_TYPE, AppConstant.TYPE_MERCHANT);
                                        SharedPrefManager.getInstance(context).saveInt(AppConstant.IS_PARENT,loginResponse.getResult().getIsParent());

                                        //Clears previous activities
                                        Utilities.clearAllgoToActivity(context, HomeActivity.class);
                                    }else if(loginResponse.getCode()==200){
//                                        new SelectOutletDialog(context,loginResponse.getList()).show();
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.USER_INFO, new Gson().toJson(loginResponse.getResult()));
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.KEY_LOGIN_USER_ID, loginResponse.getResult().getUid());
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.ACCESS_TOKEN, loginResponse.getResult().getToken());
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.USER_TYPE, AppConstant.TYPE_MERCHANT);
                                        SharedPrefManager.getInstance(context).saveInt(AppConstant.IS_PARENT,loginResponse.getResult().getIsParent());

                                        //Clears previous activities
                                        Utilities.clearAllgoToActivity(context, HomeActivity.class);
                                    }
                                    else {

                                        AlertUtil.toastMsg(context,  loginResponse.getMessage());
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
        }else{
            AlertUtil.toastMsg(context,getString(R.string.network_error));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}
