package com.icashier.app.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.icashier.app.R;
import com.icashier.app.activity.HomeActivity;
import com.icashier.app.constants.AppConstant;
import com.icashier.app.constants.ServerConstants;
import com.icashier.app.databinding.DialogChangeLangBinding;
import com.icashier.app.helper.AlertUtil;
import com.icashier.app.helper.RestClient;
import com.icashier.app.helper.SharedPrefManager;
import com.icashier.app.helper.Utilities;
import com.icashier.app.model.GenericResponse;

import java.util.HashMap;
import java.util.Locale;


/**
 * Created by admin on 2/2/2018.
 */

public class ChangeLangDialog extends Dialog {

    Activity context;
    private LayoutInflater inflater;
    DialogChangeLangBinding binding;
    View view;


    public ChangeLangDialog(Activity context, View view) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.view=view;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_change_lang, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        setCancelable(true);

      /* wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        setCancelable(false);
*/

       setLanguage();
       binding.clEnglish.setOnClickListener(v -> {

           if(!SharedPrefManager.getInstance(context).getString(AppConstant.APP_LANGUAGE,"").equals("en")) {

              callChangeLangApi("0");
           }else{
               dismiss();
           }
       });
        binding.clArabic.setOnClickListener(v -> {

            if(!SharedPrefManager.getInstance(context).getString(AppConstant.APP_LANGUAGE,"").equals("ar")){
                callChangeLangApi("1");
            }else{
                dismiss();
            }
        });

    }


    private void setLanguage() {

        String appLang=SharedPrefManager.getInstance(context).getString(AppConstant.APP_LANGUAGE,"en");


        if(appLang.equals("en"))
        {
            binding.rbEn.setChecked(true);
        }else if(appLang.equals("ar"))
        {
            binding.rbAr.setChecked(true);
        }


    }




    //==========================Method to call change online status api=====================//
    private void callChangeLangApi(String lang) {

        if(Utilities.isNetworkAvailable(context)) {
            Utilities.hideKeyboard(context);
            AlertUtil.showProgressDialog(context);

            HashMap<String, String> params = new HashMap<>();
            params.put("lang",lang);   //1 for arabic and 0 for english
            params.put("device_id",SharedPrefManager.getInstance(context).getString(AppConstant.KEY_DEVICE_TOKEN,""));
            Log.e("Lang",params.toString());

            RestClient.ApiRequest apiRequest = new RestClient.ApiRequest(context);

            apiRequest.setUrl(ServerConstants.BASE_URL + ServerConstants.CHANGE_LANG_API)
                    .setMethod(RestClient.ApiRequest.METHOD_POST)
                    .setParams(params)
                    .setResponseListener((tag, response) -> {
                        AlertUtil.hideProgressDialog();
                        if (Utilities.isValidJson(response)) {
                            GenericResponse genericResponse= new Gson().fromJson(response, GenericResponse.class);
                            if (genericResponse != null) {
                                AlertUtil.hideProgressDialog();
                                if (genericResponse.getCode()==200 ) {

                                    if(lang.equals("1")){
                                        binding.rbAr.setChecked(true);
                                        String languageToLoad  = "ar"; // your language
                                        Locale locale = new Locale(languageToLoad,"MA");
                                        Locale.setDefault(locale);
                                        Configuration config = new Configuration();
                                        config.locale = locale;

                                        context.getBaseContext().getResources().updateConfiguration(config,
                                                context.getBaseContext().getResources().getDisplayMetrics());
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.APP_LANGUAGE,"ar");
                                        dismiss();
                                        Utilities.clearAllgoToActivity(context, HomeActivity.class);
                                    }else{
                                        binding.rbEn.setChecked(true);
                                        String languageToLoad = "en"; // your language
                                        Locale locale = new Locale(languageToLoad);
                                        Locale.setDefault(locale);
                                        Configuration config = new Configuration();
                                        config.locale = locale;
                                        context.getBaseContext().getResources().updateConfiguration(config,
                                                context.getBaseContext().getResources().getDisplayMetrics());
                                        SharedPrefManager.getInstance(context).saveString(AppConstant.APP_LANGUAGE, "en");
                                        dismiss();
                                        Utilities.clearAllgoToActivity(context, HomeActivity.class);
                                    }


                                } else {

                                    AlertUtil.toastMsg(context, genericResponse.getMessage());
                                }
                            } else {
                                AlertUtil.hideProgressDialog();
                                AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                            }
                        }
                    })
                    .setErrorListener((tag, errorMsg) -> {
                        AlertUtil.hideProgressDialog();
                        AlertUtil.toastMsg(context, context.getString(R.string.error_generic));
                    })
                    .execute();
        }else
        {
            AlertUtil.toastMsg(context,context.getString(R.string.network_error));
        }
    }


}

